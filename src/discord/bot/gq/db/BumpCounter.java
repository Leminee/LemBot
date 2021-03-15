package discord.bot.gq.db;

import discord.bot.gq.Reminder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BumpCounter extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        List <MessageEmbed> disBoardEmbed = event.getMessage().getEmbeds();
        User embedAuthor = event.getAuthor();
        Message message = event.getMessage();

        if (Reminder.isSuccessBump(disBoardEmbed, embedAuthor)) {

            String embedContent = message.getEmbeds().get(0).getDescription();
            Pattern p = Pattern.compile("<@(\\d+)>");
            Matcher matcher = p.matcher(Objects.requireNonNull(embedContent));

            if (matcher.find()) {
                int bump = 1;

                String idPingedUser = matcher.group(1);
                String pingedUserName = event.getJDA().retrieveUserById(idPingedUser).complete().getName();

                try {
                    ConnectionToDB db = new ConnectionToDB();
                    db.initialize();

                    String insertUserData = "INSERT INTO user_bump (id_discord, username, number_bumps) VALUES (?,?,?);";

                    PreparedStatement userDataInput = db.connection.prepareStatement(insertUserData);
                    userDataInput.setString(1, idPingedUser);
                    userDataInput.setString(2, pingedUserName);
                    userDataInput.setInt(3, bump);

                    String verifyIfUserAlreadyExists = "SELECT id_discord FROM user_bump WHERE id_discord = ? ";
                    PreparedStatement usernameInput = db.connection.prepareStatement(verifyIfUserAlreadyExists);
                    usernameInput.setString(1, idPingedUser);
                    ResultSet rS = usernameInput.executeQuery();

                    if (rS.next()) {

                        String updateBumps = "UPDATE user_bump SET number_bumps = (number_bumps +1) WHERE id_discord = ?";
                        PreparedStatement update = db.connection.prepareStatement(updateBumps);
                        update.setString(1, idPingedUser);
                        update.executeUpdate();

                        String insertBumpTime = "INSERT INTO user_bump_time (id_user_bump_time, id_discord) VALUES (NULL,?)";
                        PreparedStatement insert = db.connection.prepareStatement(insertBumpTime);
                        insert.setString(1, idPingedUser);
                        insert.executeUpdate();

                    } else {
                        userDataInput.executeUpdate();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


