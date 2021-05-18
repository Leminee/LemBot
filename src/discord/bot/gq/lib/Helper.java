package discord.bot.gq.lib;

import discord.bot.gq.database.ConnectionToDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.Objects;

public final class Helper {
    public static int numberMember = 301;
    public static final String PREFIX = "?";
    public static final String TOKEN = "ODIwNDY4MDA5NzY0MjU3Nzky.YE1mYQ.qeNOp8qDcDfwnO-kEVZBh-thETE";

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

    public static void insertCurrentNumberMember() {
        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        String currentNumberMember = "INSERT INTO number_member (total_member) VALUES (?);";

        try {
            PreparedStatement pS = db.getConnection().prepareStatement(currentNumberMember);

            pS.setInt(1, numberMember);

            pS.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

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


}
