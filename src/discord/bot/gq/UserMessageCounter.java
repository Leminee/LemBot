package discord.bot.gq;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserMessageCounter extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String userId = Objects.requireNonNull(event.getMember()).getId();
        String userName = event.getMember().getEffectiveName();
        String messageId = event.getMessageId();

        int numberMessage = 1;

        if (!userMessage.isEmpty() && !event.getAuthor().isBot()) {

            try {
                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                String insertMessageDataQuery = "INSERT INTO user_message (id_discord, username, number_message) VALUES (?,?,?);";

                PreparedStatement userDataPStatement = db.connection.prepareStatement(insertMessageDataQuery);
                userDataPStatement.setString(1, userId);
                userDataPStatement.setString(2, userName);
                userDataPStatement.setInt(3, numberMessage);

                String verifQuery = "SELECT id_discord FROM user_message WHERE id_discord = ? ";
                PreparedStatement usernameInputPStatement = db.connection.prepareStatement(verifQuery);
                usernameInputPStatement.setString(1, userId);
                ResultSet rS = usernameInputPStatement.executeQuery();

                if (rS.next()) {

                    String updateQuery = "UPDATE user_message SET number_message = (number_message +1) WHERE id_discord = ?";
                    PreparedStatement updatePStatement = db.connection.prepareStatement(updateQuery);
                    updatePStatement.setString(1, userId);
                    updatePStatement.executeUpdate();

                    String insertQuery = "INSERT INTO user_message_content (id_message, id_discord, content) VALUES (?,?,?)";
                    PreparedStatement insertPStatement = db.connection.prepareStatement(insertQuery);
                    insertPStatement.setString(1, messageId);
                    insertPStatement.setString(2, userId);
                    byte[] byteA = userMessage.getBytes();
                    Blob blob = insertPStatement.getConnection().createBlob();
                    blob.setBytes(1, byteA);
                    insertPStatement.setBlob(3, blob);

                    insertPStatement.executeUpdate();

                } else {
                    userDataPStatement.executeUpdate();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
}
