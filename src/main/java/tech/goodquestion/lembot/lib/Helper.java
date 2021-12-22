package tech.goodquestion.lembot.lib;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandsHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.entity.UserData;

import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Helper {

    public static final String PREFIX = "?";


    public static boolean isNotSuccessfulBump(final List<MessageEmbed> messages, final User embedAuthor) {

        final long disBoardId = Config.getInstance().getUser().getDisboardId();
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

            CommandsHelper.logException(OccurredException.getOccurredExceptionData(sqlException, Helper.class.getName()));
        }

    }

    public static void sendAmount(final UserData userData,final String embedColor, final String amountOf, final TextChannel channel, final String embedTitle) {

        final String authorMention = "<@" + userData.userId + ">";

        if (!userData.hasBump()) {
            channel.sendMessage("Du hast leider noch keinen erfolgreichen Bump " + authorMention).queue();
            return;
        }

        if (userData.isTop()) {
            channel.sendMessage(" :first_place: Du bist **TOP 1** mit " + userData.amountOf + " " + amountOf + " " + authorMention).queue();
            return;
        }

       final String mentionedUser = CommandManager.getInstance().getJDA().retrieveUserById(userData.nextHigherUserId).complete().getAsMention();

        final EmbedBuilder numberInfo = new EmbedBuilder();
        numberInfo.setColor(Color.decode(embedColor));
        numberInfo.setTitle(embedTitle);
        numberInfo.setDescription("Anzahl deiner " + amountOf + " **" + userData.amountOf + "**" + " " + authorMention + "\n" + "Du bist hinter dem User " + mentionedUser + " **(" + userData.nextHigherUserAmountOf + " " + amountOf + ")**");

        channel.sendMessage(numberInfo.build()).queue();
    }

    public static void scheduleCommand(final String command,final int delay, final long period, final TimeUnit timeUnit) {
        scheduleCommand(command, delay, period, timeUnit, new String[0]);
    }

    public static void scheduleCommand(final String command, final int delay, final long period, final TimeUnit timeUnit, final String[] args) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        IBotCommand botCommand = CommandManager.getInstance().getCommand(command);

        if (botCommand == null) return;

        final Runnable commandRunner = () -> {
            try {
                botCommand.dispatch(null, Config.getInstance().getChannel().getBumpChannel(), null, args);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };

        scheduler.scheduleAtFixedRate(commandRunner, delay, period, timeUnit);
    }

    public static void createEmbed(final EmbedBuilder embedBuilder, final String title, final String description, final String embedColor, final String thumbnail) {
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.decode(embedColor));
        embedBuilder.setThumbnail(thumbnail);
    }

    public static void createEmbed(final EmbedBuilder embedBuilder, final String title, final String description, final String embedColor) {
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.decode(embedColor));
    }



    public static void addTopToEmbed(ResultSet resultSet,final EmbedBuilder embedBuilder, final String embedTitle, final String embedDescription, final String embedThumbnail, final String embedColor, final TextChannel channel, final String amountOf) {
        createEmbed(embedBuilder, embedTitle, embedDescription, embedColor, embedThumbnail);
        addTopToEmbed(resultSet, embedBuilder, channel, amountOf);
    }

    public static void addTopToEmbed(ResultSet resultSet, final EmbedBuilder embedBuilder, final TextChannel channel, final String amountOf) {
        int top = 1;
        try {
            while (resultSet.next()) {
                embedBuilder.addField("TOP " + top, resultSet.getString(1) + " **(" + resultSet.getString(2) + " " + amountOf + ")**", false);
                top++;
            }

            channel.sendMessage(embedBuilder.build()).queue();

        } catch (SQLException sqlException) {

            System.out.println(sqlException.getMessage());
            CommandsHelper.logException(OccurredException.getOccurredExceptionData(sqlException, Helper.class.getName()));
        }
    }

    public static String getCurrentDateTime() {
        final Date date = new Date();

        return date.toString().substring(11, 16);
    }


    public static String getLemBotContributorsCount() {

        final String base_url = "https://github.com/Leminee/LemBot";

        try {
           final Document document = Jsoup.connect(base_url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36")
                    .get();
           final Elements element = document.getElementsByClass("Counter");

            return element.get(element.size() - 1).text();

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
        return "-1";
    }

    public static String getGermanDateTime() {

        return LocalDateTime.now().getDayOfMonth()
                + "-" + LocalDateTime.now().getMonth().getValue()
                + "-" + LocalDateTime.now().getYear()
                + "-" + LocalDateTime.now().getHour()
                + ":" + LocalDateTime.now().getMinute();
    }
}
