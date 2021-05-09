package discord.bot.gq.lib;

import discord.bot.gq.BotMain;
import discord.bot.gq.database.ConnectionToDB;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public final class Helper {

    private Helper() {
    }

    public static boolean isValidCommand(String userMessage, String command) {

        return userMessage.equalsIgnoreCase(BotMain.PREFIX + command);
    }

    public static boolean isSuccessBump(List<MessageEmbed> messages, User embedAuthor) {

        long disBoardId = 302050872383242240L;
        String successBumpImageUrl = "https://disboard.org/images/bot-command-image-bump.png";

        if (embedAuthor.getIdLong() != disBoardId) {
            return false;
        }

        if (messages.get(0).getDescription() == null) {
            return false;
        }

        if (messages.get(0).getImage() == null) {
            return false;
        }
        return Objects.equals(Objects.requireNonNull(messages.get(0).getImage()).getUrl(), successBumpImageUrl);
    }

    public static Blob changeCharacterEncoding(PreparedStatement userDataInput, String userName) throws SQLException {
        byte[] byteA = userName.getBytes();
        Blob blob = userDataInput.getConnection().createBlob();
        blob.setBytes(1, byteA);

        return blob;
    }

    public static void storeDataIntoDb(FileReader file, String insertQuery) throws IOException {
        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        try (BufferedReader bR = new BufferedReader(file)) {

            String line;

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(insertQuery);

            while ((line = bR.readLine()) != null) {

                preparedStatement.setBlob(1, changeCharacterEncoding(preparedStatement, line));
                preparedStatement.executeUpdate();
            }

        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

}
