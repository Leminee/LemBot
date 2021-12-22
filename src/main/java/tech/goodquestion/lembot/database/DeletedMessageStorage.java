package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.entity.OccurredException;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeletedMessageStorage extends ListenerAdapter {

    @Override
    public void onGuildMessageDelete(@Nonnull final GuildMessageDeleteEvent event) {
       final long idDeletedMessage = event.getMessageIdLong();

        Connection connection = DatabaseConnector.openConnection();
        final String messageStored = "SELECT id_message FROM user_message_content WHERE id_message = ?";
        final String deletedMessage = "INSERT INTO deleted_message (id_deleted_message) VALUES (?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(messageStored);
            preparedStatement.setLong(1, idDeletedMessage);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                PreparedStatement preparedStatementOne = connection.prepareStatement(deletedMessage);
                preparedStatementOne.setLong(1, idDeletedMessage);
                preparedStatementOne.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandsHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }

}
