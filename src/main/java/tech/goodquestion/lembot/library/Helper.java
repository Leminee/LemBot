package tech.goodquestion.lembot.library;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.entity.UserData;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class Helper {

    private Helper() {

    }

    public static boolean isNotSuccessfulBump(final List<MessageEmbed> messages, final User embedAuthor) {

        final long disBoardId = Config.getInstance().getGuild().getJDA().retrieveUserById(302050872383242240L).complete().getIdLong();
        final String successfulBumpImageUrl = "https://disboard.org/images/bot-command-image-bump.png";

        if (embedAuthor.getIdLong() != disBoardId) {
            return true;
        }

        if (messages.get(0).getDescription() == null) {
            return true;
        }

        if (messages.get(0).getImage() == null) {
            return true;
        }

        return !Objects.equals(Objects.requireNonNull(messages.get(0).getImage()).getUrl(), successfulBumpImageUrl);
    }

    public static Blob changeCharacterEncoding(final PreparedStatement userDataInput, final String userName) throws SQLException {
        final byte[] byteA = userName.getBytes();
        final Blob blob = userDataInput.getConnection().createBlob();
        blob.setBytes(1, byteA);

        return blob;
    }

    public static void getAmount(final UserData userData, final String query, final String nextHigherUser) {

        Connection connection = DatabaseConnector.openConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, userData.userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                userData.amountOf = resultSet.getLong(1);

                PreparedStatement prepareStatementOne = connection.prepareStatement(nextHigherUser);
                prepareStatementOne.setLong(1, userData.amountOf);

                ResultSet resultSetOne = prepareStatementOne.executeQuery();

                if (resultSetOne.next()) {
                    userData.nextHigherUserId = resultSetOne.getLong(1);
                    userData.nextHigherUserAmountOf = resultSetOne.getInt(2);
                }
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, Helper.class.getName()));
        }

    }

    public static void sendAmount(final UserData userData, final String embedColor, final String amountOf, final Message message, final String embedTitle) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.decode(embedColor));
        embedBuilder.setTitle(embedTitle);

        if (!userData.hasBump()) {
            embedBuilder.setDescription("Du hast noch keinen erfolgreichen Bump");

            sendEmbed(embedBuilder, message, true);
            return;
        }

        if (userData.isTop()) {

            embedBuilder.setDescription(" :first_place: Du bist **TOP 1** mit " + userData.amountOf + " " + amountOf);
            sendEmbed(embedBuilder, message, true);
            return;
        }

        final String mentionedUser = CommandManager.getInstance().getJDA().retrieveUserById(userData.nextHigherUserId).complete().getAsMention();

        embedBuilder.setDescription("Anzahl deiner " + amountOf + " **" + userData.amountOf + "**" + "\n" + "Du bist hinter dem User " + mentionedUser + " **(" + userData.nextHigherUserAmountOf + ")**");

        message.replyEmbeds(embedBuilder.build()).queue();
    }

    public static void createEmbed(final EmbedBuilder embedBuilder, final String title, final String description, final String embedColor, final String thumbnail) {
        createEmbed(embedBuilder, title, description, embedColor);
        embedBuilder.setThumbnail(thumbnail);
    }

    public static void createEmbed(final EmbedBuilder embedBuilder, final String title, final String description, final String embedColor) {
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.decode(embedColor));
    }

    public static void sendEmbed(final EmbedBuilder embedBuilder, final Message message, final boolean reply) {

        if (reply) {
            message.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        message.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public static void addTopToEmbed(ResultSet resultSet, final EmbedBuilder embedBuilder, final String embedTitle, final String embedDescription, final String embedThumbnail, final String embedColor, final Message message, final String amountOf) {
        createEmbed(embedBuilder, embedTitle, embedDescription, embedColor, embedThumbnail);
        addTopToEmbed(resultSet, embedBuilder, message, amountOf);
    }

    public static void addTopToEmbed(ResultSet resultSet, final EmbedBuilder embedBuilder, final Message message, final String amountOf) {
        int top = 1;

        try {
            while (resultSet.next()) {

                embedBuilder.addField("TOP " + top, "<@" + resultSet.getString(1) + "> " + "**(" + resultSet.getString(2) + " " + amountOf + ")**", false);
                top++;
            }

            sendEmbed(embedBuilder, message, true);

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, Helper.class.getName()));
        }
    }

    public static String getCurrentCETDateTime() {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(Locale.GERMAN);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.GERMAN);
        String dateNow = LocalDate.now().format(dateFormatter);
        String timeNow = LocalTime.now().format(timeFormatter);

        return dateNow + " um " + timeNow;
    }

    @SuppressWarnings("null")
    public static User getUserFromCommandInput(final Message message, final String[] args) {

        List<User> mentionedUsers = message.getMentionedUsers();
        User user;

        if (mentionedUsers.isEmpty()) {
            user = CommandManager.getInstance().getJDA().retrieveUserById(args[0], true).complete();
        } else {
            user = mentionedUsers.get(0);
        }

        return user;
    }

    public static boolean isStaffChannel(Message message, TextChannel channel) {
        if (channel.getIdLong() != Config.getInstance().getChannelConfig().getStaffCommandsChannel().getIdLong()) {
            Helper.sendError(message, ":x: Dieser Befehl kann nur in [channel] ausgeführt werden!".replace("[channel]", Config.getInstance().getChannelConfig().getStaffCommandsChannel().getAsMention()));
            return true;
        }
        return false;
    }

    public static void addTopCommandToEmbed(ResultSet resultSet, final EmbedBuilder embedBuilder, final Message message, final String amountOf) {
        int top = 1;

        try {
            while (resultSet.next()) {

                embedBuilder.addField("TOP " + top, "`" + resultSet.getString(1) + "` " + "**(" + resultSet.getString(2) + amountOf + ")**", false);
                top++;
            }

            sendEmbed(embedBuilder, message, true);

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, Helper.class.getName()));
        }
    }

    public static void sendError(final Message message, final String embedDescription) {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
        Helper.sendEmbed(embedBuilder, message, true);
    }
}