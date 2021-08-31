package discord.bot.gq.lib;

import discord.bot.gq.config.db.ConfigSelection;
import discord.bot.gq.database.ConnectionToDB;
import discord.bot.gq.entities.Sanction;
import discord.bot.gq.entities.UserData;
import discord.bot.gq.entities.VoiceChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Helper {

    public static final String PREFIX = "?";
    public static final String TOKEN = "ODIwNDY4MDA5NzY0MjU3Nzky.YE1mYQ.ShmaUjLMh0oBcn-KUQEBjOwbDkA";

    private Helper() {

    }

    public static boolean isValidCommand(String userMessageContent, String command) {

        return userMessageContent.toLowerCase().startsWith(PREFIX + command);
    }

    public static boolean isSuccessfulBump(List<MessageEmbed> messages, User embedAuthor) {

        long disBoardId = 302050872383242240L;
        String successfulBumpImageUrl = "https://disboard.org/images/bot-command-image-bump.png";

        if (embedAuthor.getIdLong() != disBoardId) {
            return false;
        }

        if (messages.get(0).getDescription() == null) {
            return false;
        }

        if (messages.get(0).getImage() == null) {
            return false;
        }
        return Objects.equals(Objects.requireNonNull(messages.get(0).getImage()).getUrl(), successfulBumpImageUrl);
    }

    public static Blob changeCharacterEncoding(PreparedStatement userDataInput, String userName) throws SQLException {
        byte[] byteA = userName.getBytes();
        Blob blob = userDataInput.getConnection().createBlob();
        blob.setBytes(1, byteA);

        return blob;
    }

    public static void insertCurrentNumberMember(int currentNumber) {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String currentNumberMember = "INSERT INTO number_member (total_member) VALUES (?);";

        try {
            PreparedStatement pS = connectionToDB.getConnection().prepareStatement(currentNumberMember);

            pS.setInt(1, currentNumber);

            pS.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void selectTop(String command, GuildMessageReceivedEvent event, String topQuery, String embedTitle, Color embedColor, String thumbnail) {

        String userMessage = event.getMessage().getContentRaw();

        if (Helper.isValidCommand(userMessage, command)) {
            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            try (Statement statement = db.getConnection().createStatement(); ResultSet rS = statement.executeQuery(topQuery)) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(embedTitle);
                embedBuilder.setDescription("");
                embedBuilder.setColor(embedColor);
                embedBuilder.setThumbnail(thumbnail);
                String top1, top2, top3, n1, n2, n3;

                if (command.equals("top") || command.equals("topb")) {
                    top1 = ":first_place:";
                    top2 = ":second_place:";
                    top3 = ":third_place:";
                    n1 = "";
                    n2 = "";
                    n3 = "";
                } else {
                    top1 = "";
                    top2 = "";
                    top3 = "";
                    n1 = "TOP 1";
                    n2 = "TOP 2";
                    n3 = "TOP 3";
                }

                int top = 1;

                while (rS.next()) {
                    if (top == 1) {
                        embedBuilder.addField(n1, top1 + rS.getString(1), false);
                    }
                    if (top == 2) {
                        embedBuilder.addField(n2, top2 + rS.getString(1), false);
                    }
                    if (top == 3) {
                        embedBuilder.addField(n3, top3 + rS.getString(1), false);
                    }
                    top++;
                }
                event.getChannel().sendMessage(embedBuilder.build()).queue();

            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
    }


    public static void getAmount(UserData userData, String query, String nextHigherUser, GuildMessageReceivedEvent event) {


        userData.userId = event.getAuthor().getIdLong();


        if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

            ConnectionToDB connectionToDB = new ConnectionToDB();
            connectionToDB.initialize();

            try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(query)) {


                preparedStatement.setLong(1, userData.userId);

                ResultSet resultSet = preparedStatement.executeQuery();


                if (resultSet.next()) {

                    userData.amountOf = resultSet.getInt(1);

                    PreparedStatement pSTwo = connectionToDB.getConnection().prepareStatement(nextHigherUser);
                    pSTwo.setLong(1, userData.amountOf);
                    ResultSet rSTwo = pSTwo.executeQuery();

                    if (rSTwo.next()) {

                        userData.nextHigherUserId = rSTwo.getLong(1);
                        userData.nextHigherUserAmountOf = rSTwo.getInt(2);

                    }
                }

            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }

        }

    }

    public static void sendAmount(UserData userData, GuildMessageReceivedEvent event, String embedColor, String amountOf) {


        String authorCommand = event.getAuthor().getAsMention();


        if (isTopOne(userData)) {
            event.getChannel().sendMessage(" :first_place: Du bist **TOP 1** mit " + userData.amountOf + " " + amountOf + " " + authorCommand).queue();
            return;
        }

        String mentionedUser = event.getJDA().retrieveUserById(userData.nextHigherUserId).complete().getAsMention();

        EmbedBuilder numberInfo = new EmbedBuilder();
        numberInfo.setColor(Color.decode(embedColor));
        numberInfo.setTitle("Information");
        numberInfo.setDescription("Anzahl deiner " + amountOf + " **" + userData.amountOf + "**" + " " + authorCommand + "\n" + "Du bist hinter dem User " + mentionedUser + " **(" + userData.nextHigherUserAmountOf + " " + amountOf + ")**");

        event.getChannel().sendMessage(numberInfo.build()).queue();


    }

    public static boolean isTopOne(UserData userData) {
        return userData.nextHigherUserId == null || userData.nextHigherUserAmountOf == null;
    }


    public static void sendCommand(String command, GuildMessageReceivedEvent event, int delay, int period, TimeUnit timeUnit) {


        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        ConfigSelection configSelection = new ConfigSelection();
        configSelection.selectBotCommandsChannelId();

        final Runnable ping = () -> Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getTextChannelById(configSelection.getBotCommandsChannelId())).sendMessage(Helper.PREFIX + command)).queue();
        scheduler.scheduleAtFixedRate(ping, delay, period, timeUnit);

    }

    public static void insertStatus(long userId, String userTag, OnlineStatus newStatus) {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String currentStatus = "INSERT INTO user_status (id_discord, user_tag, status) VALUES (?,?,?);";

        try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(currentStatus)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setBlob(2, changeCharacterEncoding(preparedStatement, userTag));
            preparedStatement.setString(3, String.valueOf(newStatus));

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static void insertNumberOnlineMember(int numberMember) {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String currentPresentMember = "INSERT INTO active_user (active_member) VALUES (?);";

        try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(currentPresentMember)) {


            preparedStatement.setLong(1, numberMember);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

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

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        try (PreparedStatement pS = connectionToDB.getConnection().prepareStatement(insertQuery)) {


            pS.setLong(1, voiceChannel.userId);
            pS.setString(2, voiceChannel.userTag);
            pS.setString(3, voiceChannel.userName);
            pS.setString(4, voiceChannel.name);
            pS.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

    }

    public static void insertSanctionedUserData(String insertQuery, Sanction sanction) {

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(insertQuery)) {


            preparedStatement.setLong(1, sanction.userId);
            preparedStatement.setString(2, sanction.userTag);
            preparedStatement.setString(3, sanction.userName);
            preparedStatement.setString(4, sanction.author);
            preparedStatement.setString(5, sanction.reason);
            preparedStatement.setString(6, sanction.channelName);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

    }

    public static void sendDM(User sanctionedUser, String typeSanction, StringBuilder reason, String mentionedUser) {

        String content = "Du wurdest aus dem folgenden Grund auf GoodQuestion " + "**" + typeSanction + "**" + ": " + reason + mentionedUser;
        sanctionedUser.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .queue();
    }

    public static void addTopToEmbed(GuildMessageReceivedEvent event, ResultSet resultSet, EmbedBuilder embedBuilder, String embedTitle, String embedDescription, String embedThumbnail, Color embedColor) {

        Helper.createEmbed(embedBuilder, embedTitle, embedDescription, embedColor, embedThumbnail);

        int top = 1;
        try {

            while (resultSet.next()) {

                embedBuilder.addField("TOP " + top, resultSet.getString(1), false);
                top++;

            }

            event.getChannel().sendMessage(embedBuilder.build()).queue();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }
}