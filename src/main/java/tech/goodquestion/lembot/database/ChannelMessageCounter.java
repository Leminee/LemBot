package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.entity.OccurredException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public final class ChannelMessageCounter extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {

        final String userMessage = event.getMessage().getContentRaw();
        final String channelId = event.getChannel().getId();
        final String channelName = Objects.requireNonNull(event.getJDA().getTextChannelById(channelId)).getName();
        final String messageId = event.getMessageId();

        if (userMessage.isEmpty()) return;

        Connection connection = DatabaseConnector.openConnection();
        final String insertChannel = "INSERT INTO channel (id_message, id_channel, name) VALUES (?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertChannel)) {

            preparedStatement.setString(1, messageId);
            preparedStatement.setString(2, channelId);
            preparedStatement.setString(3, channelName);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }
}
