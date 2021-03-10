package discord.bot.gq;

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


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        List<MessageEmbed> messages = event.getMessage().getEmbeds();
        User author = event.getAuthor();
        Message m = event.getMessage();

        if (!author.getId().equals("302050872383242240")) {
            return;
        }
        if (messages.isEmpty()) {
            return;
        }
        if (messages.get(0).getDescription() == null) {
            return;
        }
        if (!Objects.requireNonNull(messages.get(0).getDescription()).contains("https://disboard.org/")) {
            return;
        }

        String embedContent = m.getEmbeds().get(0).getDescription();
        Pattern p = Pattern.compile("<@(\\d+)>");
        Matcher matcher = p.matcher(Objects.requireNonNull(embedContent));

        if(matcher.find()) {
            int bump = 1;

            String idPingedUser = matcher.group(1);
            String pingedUserName = event.getJDA().retrieveUserById(idPingedUser).complete().getName();

            try {
                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                String insertAccountQuery = "INSERT INTO user_bump (id_discord, username, number_bumps) VALUES (?,?,?);";

                PreparedStatement userDataInputPStatement = db.connection.prepareStatement(insertAccountQuery);
                userDataInputPStatement.setString(1, idPingedUser);
                userDataInputPStatement.setString(2, pingedUserName);
                userDataInputPStatement.setInt(3, bump);

                String verifyIfUsernameAlreadyExistsQuery = "SELECT id_discord FROM user_bump WHERE id_discord = ? ";
                PreparedStatement usernameInputPStatement = db.connection.prepareStatement(verifyIfUsernameAlreadyExistsQuery);
                usernameInputPStatement.setString(1, idPingedUser);
                ResultSet rS = usernameInputPStatement.executeQuery();

                if (rS.next()) {

                    String updateQuery = "UPDATE user_bump SET number_bumps = (number_bumps +1) WHERE id_discord = ?";
                    PreparedStatement updatePStatement = db.connection.prepareStatement(updateQuery);
                    updatePStatement.setString(1, idPingedUser);
                    updatePStatement.executeUpdate();

                    String insertQuery = "INSERT INTO user_bump_time (id_user_bump_time, id_discord) VALUES (NULL,?)";
                    PreparedStatement insertPStatement = db.connection.prepareStatement(insertQuery);
                    insertPStatement.setString(1, idPingedUser);
                    insertPStatement.executeUpdate();

                } else {
                    userDataInputPStatement.executeUpdate();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



        }


    }


