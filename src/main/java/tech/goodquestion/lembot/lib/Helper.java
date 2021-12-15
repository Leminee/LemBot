package tech.goodquestion.lembot.lib;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.entities.UserData;
import tech.goodquestion.lembot.entities.VoiceChannel;

import java.awt.*;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Helper {

    public static final String PREFIX = "?";


    public static boolean isNotSuccessfulBump(List<MessageEmbed> messages, User embedAuthor) {

        final long disBoardId = Config.getInstance().getUser().getDisboardId();
        String successfulBumpImageUrl = "https://disboard.org/images/bot-command-image-bump.png";

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

    public static Blob changeCharacterEncoding(PreparedStatement userDataInput, String userName) throws SQLException {
        final byte[] byteA = userName.getBytes();
        Blob blob = userDataInput.getConnection().createBlob();
        blob.setBytes(1, byteA);

        return blob;
    }

    public static void getAmount(UserData userData, String query, String nextHigherUser) {

        Connection connection = DatabaseConnector.openConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, userData.userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                userData.amountOf = resultSet.getInt(1);

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
        }

    }

    public static void sendAmount(UserData userData, String embedColor, String amountOf, TextChannel channel) {

        String authorMention = "<@" + userData.userId + ">";

        if (!userData.hasBump()) {
            channel.sendMessage("Du hast leider noch keinen erfolgreichen Bump " + authorMention).queue();
            return;
        }

        if (userData.isTop()) {
            channel.sendMessage(" :first_place: Du bist **TOP 1** mit " + userData.amountOf + " " + amountOf + " " + authorMention).queue();
            return;
        }

        String mentionedUser = CommandManager.getInstance().getJDA().retrieveUserById(userData.nextHigherUserId).complete().getAsMention();

        EmbedBuilder numberInfo = new EmbedBuilder();
        numberInfo.setColor(Color.decode(embedColor));
        numberInfo.setTitle("Information");
        numberInfo.setDescription("Anzahl deiner " + amountOf + " **" + userData.amountOf + "**" + " " + authorMention + "\n" + "Du bist hinter dem User " + mentionedUser + " **(" + userData.nextHigherUserAmountOf + " " + amountOf + ")**");

        channel.sendMessage(numberInfo.build()).queue();
    }

    public static void scheduleCommand(String command, int delay, long period, TimeUnit timeUnit) {
        scheduleCommand(command, delay, period, timeUnit, new String[0]);
    }

    public static void scheduleCommand(String command, int delay, long period, TimeUnit timeUnit, String[] args) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        IBotCommand botCommand = CommandManager.getInstance().getCommand(command);

        if (botCommand == null) return;

        final Runnable commandRunner = () -> botCommand.dispatch(null, Config.getInstance().getChannel().getBumpChannel(), null, args);

        scheduler.scheduleAtFixedRate(commandRunner, delay, period, timeUnit);
    }

    public static void createEmbed(EmbedBuilder embedBuilder, String title, String description, Color color, String thumbnail) {
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(color);
        embedBuilder.setThumbnail(thumbnail);
    }

    public static void createEmbed(EmbedBuilder embedBuilder, String title, String description, Color color) {
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(color);
    }

    public static void insertVoiceChannelData(String insertQuery, VoiceChannel voiceChannel) {

        Connection connection = DatabaseConnector.openConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setLong(1, voiceChannel.userId);
            preparedStatement.setString(2, voiceChannel.userTag);
            preparedStatement.setString(3, voiceChannel.userName);
            preparedStatement.setString(4, voiceChannel.name);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void addTopToEmbed(ResultSet resultSet, EmbedBuilder embedBuilder, String embedTitle, String embedDescription, String embedThumbnail, Color embedColor, TextChannel channel, String amountOf) {

        Helper.createEmbed(embedBuilder, embedTitle, embedDescription, embedColor, embedThumbnail);

        int top = 1;
        try {
            while (resultSet.next()) {
                embedBuilder.addField("TOP " + top, resultSet.getString(1) + " **(" +resultSet.getString(2)+ " " +amountOf +")**", false);
                top++;
            }

            channel.sendMessage(embedBuilder.build()).queue();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static String getCurrentTime(){
        java.util.Date date = new Date();

        return date.toString().substring(11, 16);
    }

    public static void addTopMonthlyDataToEmbed(TextChannel channel, ResultSet resultSet, EmbedBuilder topBumperEmbed, String embedTitle, String embedDescription, String embedThumbnail, Color embedColor, String amountOf) {
        createEmbed(topBumperEmbed, embedTitle, embedDescription, embedColor, embedThumbnail);

        int top = 1;
        try {
            while (resultSet.next()) {
                topBumperEmbed.addField("TOP " + top, resultSet.getString(1) + " **(" +resultSet.getString(2)+ " " +amountOf +")**", false);
                top++;
            }

            channel.sendMessage(topBumperEmbed.build()).queue();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }
}
