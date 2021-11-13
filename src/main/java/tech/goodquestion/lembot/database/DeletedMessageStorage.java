package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeletedMessageStorage extends ListenerAdapter {

    @Override
    public void onGuildMessageDelete(@Nonnull GuildMessageDeleteEvent event) {
        long idDeletedMessage = event.getMessageIdLong();

        Connection connection = DatabaseConnector.openConnection();
        String messageStored = "SELECT id_message FROM user_message_content WHERE id_message = ?";
        String deletedMessage = "INSERT INTO deleted_message (id_deleted_message) VALUES (?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(messageStored);
            preparedStatement.setLong(1, idDeletedMessage);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                PreparedStatement preparedStatementOne = connection.prepareStatement(deletedMessage);
                preparedStatementOne.setLong(1, idDeletedMessage);
                preparedStatementOne.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

}