package discord.bot.gq.db;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserMessageCounter extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String userId = Objects.requireNonNull(event.getMember()).getId();
        String userName = event.getMember().getEffectiveName();
        String messageId = event.getMessageId();

        int numberMessage = 1;

        if (!userMessage.isEmpty()) {

            try {
                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                String insertMessageData = "INSERT INTO user_message (id_discord, username, number_message) VALUES (?,?,?);";

                PreparedStatement userData = db.connection.prepareStatement(insertMessageData);
                userData.setString(1, userId);
                userData.setString(2, userName);
                userData.setInt(3, numberMessage);

                String verifQuery = "SELECT id_discord FROM user_message WHERE id_discord = ? ";
                PreparedStatement usernameInput = db.connection.prepareStatement(verifQuery);
                usernameInput.setString(1, userId);
                ResultSet rS = usernameInput.executeQuery();

                if (rS.next()) {

                    String updateQuery = "UPDATE user_message SET number_message = (number_message +1) WHERE id_discord = ?";
                    PreparedStatement updatePStatement = db.connection.prepareStatement(updateQuery);
                    updatePStatement.setString(1, userId);
                    updatePStatement.executeUpdate();


                } else {
                    userData.executeUpdate();

                }
                insertData(userMessage, userId,messageId);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
    public void insertData(String userMessage, String userId, String messageId) throws SQLException {

        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        String insertQuery = "INSERT INTO user_message_content (id_message, id_discord, content) VALUES (?,?,?)";
        PreparedStatement insertPStatement = db.connection.prepareStatement(insertQuery);
        insertPStatement.setString(1, messageId);
        insertPStatement.setString(2, userId);
        insertPStatement.setBlob(3,StorageMemberLeave.setTransformedString(insertPStatement,userMessage));
        insertPStatement.executeUpdate();

    }
}
