package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.lib.Helper;

import java.sql.Connection;
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
        List<MessageEmbed> disBoardEmbed = event.getMessage().getEmbeds();
        User embedAuthor = event.getAuthor();
        Message message = event.getMessage();
        String pingedUser = "<@(\\d+)>";

        if (!Helper.isSuccessfulBump(disBoardEmbed, embedAuthor)) return;

        String embedContent = message.getEmbeds().get(0).getDescription();
        Pattern p = Pattern.compile(pingedUser);
        Matcher matcher = p.matcher(Objects.requireNonNull(embedContent));

        if (!matcher.find()) return;

        int bump = 1;
        String idPingedUser = matcher.group(1);
        String pingedUserName = event.getJDA().retrieveUserById(idPingedUser).complete().getName();

        Connection connection = DatabaseConnector.openConnection();
        String userExists = "SELECT id_discord FROM user_bump WHERE id_discord = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(userExists)) {
            preparedStatement.setString(1, idPingedUser);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String currentNumberBump = "UPDATE user_bump SET number_bumps = (number_bumps +1) WHERE id_discord = ?";
                PreparedStatement prepareStatementOne = connection.prepareStatement(currentNumberBump);
                prepareStatementOne.setString(1, idPingedUser);
                prepareStatementOne.executeUpdate();

                String bumpTime = "INSERT INTO user_bump_time (id_user_bump_time, id_discord) VALUES (NULL,?)";
                PreparedStatement insert = connection.prepareStatement(bumpTime);
                insert.setString(1, idPingedUser);
                insert.executeUpdate();
            } else {
                String bumpData = "INSERT INTO user_bump (id_discord, username, number_bumps) VALUES (?,?,?);";

                PreparedStatement prepareStatementThree = connection.prepareStatement(bumpData);
                prepareStatementThree.setString(1, idPingedUser);
                prepareStatementThree.setString(2, pingedUserName);
                prepareStatementThree.setInt(3, bump);
                prepareStatementThree.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

}


