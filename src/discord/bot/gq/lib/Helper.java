package discord.bot.gq.lib;

import discord.bot.gq.config.db.ConfigSelection;
import discord.bot.gq.database.ConnectionToDB;
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
    public static final String TOKEN = "";

    private Helper() {
    }

    public static boolean isValidCommand(String userMessage, String command) {
        return userMessage.equalsIgnoreCase(PREFIX + command);
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
        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        String currentNumberMember = "INSERT INTO number_member (total_member) VALUES (?);";

        try {
            PreparedStatement pS = db.getConnection().prepareStatement(currentNumberMember);

            pS.setInt(1, currentNumber);

            pS.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    //TODO
    public static void selectTop(String topQuery, String embedTitle, String embedColor, String embedThumbnail) {

        try {

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            Statement statement = db.getConnection().createStatement();
            ResultSet rS = statement.executeQuery(topQuery);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(embedTitle);
            embedBuilder.setDescription("");
            embedBuilder.setColor(Color.decode(embedColor));
            embedBuilder.setThumbnail(embedThumbnail);

            int top = 1;

            while (rS.next()) {

                embedBuilder.addField("TOP " + top, rS.getString(1), false);
                top++;

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void sendAmount(String query, String nextUser,String command, GuildMessageReceivedEvent event, String embedColor, String string) {


        String userMessage = event.getMessage().getContentRaw();
        String authorCommand = event.getAuthor().getAsMention();
        long authorId = event.getAuthor().getIdLong();

        if (Helper.isValidCommand(userMessage, command)) {
            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                try {

                    PreparedStatement pS = db.getConnection().prepareStatement(query);
                    pS.setLong(1, authorId);
                    ResultSet rS = pS.executeQuery();


                    if (rS.next()) {

                        int number = rS.getInt(1);

                        PreparedStatement pSTwo = db.getConnection().prepareStatement(nextUser);
                        pSTwo.setLong(1, number);
                        ResultSet rSTwo = pSTwo.executeQuery();
                       
                        if (rSTwo.next()) {

                            long nextTopUserId = rSTwo.getLong(1);
                            String mentionedUser = event.getJDA().retrieveUserById(nextTopUserId).complete().getAsMention();
                            int nextTopUser= rSTwo.getInt(2);

                            EmbedBuilder numberInfo = new EmbedBuilder();
                            numberInfo.setColor(Color.decode(embedColor));
                            numberInfo.setTitle("Information");
                            numberInfo.setDescription("Anzahl deiner " +  string  + " **" + number + "**" + " " + authorCommand + "\n" + "Du bist hinter dem User " + mentionedUser + " **(" + nextTopUser + " " +  string + ")**");
                            event.getChannel().sendMessage(numberInfo.build()).queue();
                        }


                        else {
                            event.getChannel().sendMessage(" :first_place: Du bist **TOP 1** mit " + number + " " +  string + " " + authorCommand).queue();
                        }

                    }
                    pS.close();
                    rS.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public static void sendCommand(String command, GuildMessageReceivedEvent event, int delay, int period, TimeUnit timeUnit){


            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            ConfigSelection configSelection = new ConfigSelection();
            configSelection.selectChannelId();

            final Runnable ping = () -> Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getTextChannelById(configSelection.getChannelId())).sendMessage(Helper.PREFIX + command)).queue();
            scheduler.scheduleAtFixedRate(ping, delay, period, timeUnit);

    }

    public static void insertStatus(long userId, String userTag,OnlineStatus newStatus) {
        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        String currentStatus = "INSERT INTO user_status (id_discord, user_tag, status) VALUES (?,?,?);";

        try {
            PreparedStatement pS = db.getConnection().prepareStatement(currentStatus);

            pS.setLong(1, userId);
            pS.setBlob(2,changeCharacterEncoding(pS,userTag));
            pS.setString(3,String.valueOf(newStatus));

            pS.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void insertNumberOnlineMember(int numberMember) {
        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        String currentPresentMember = "INSERT INTO active_user (active_member) VALUES (?);";

        try {
            PreparedStatement pS = db.getConnection().prepareStatement(currentPresentMember);

            pS.setLong(1, numberMember);

            pS.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}