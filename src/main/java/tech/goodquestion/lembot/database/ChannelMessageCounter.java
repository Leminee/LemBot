package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.entity.OccurredException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class ChannelMessageCounter extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String userMessage = event.getMessage().getContentRaw();
        String channelId = event.getChannel().getId();
        String channelName = Objects.requireNonNull(event.getJDA().getTextChannelById(channelId)).getName();
        String messageId = event.getMessageId();

        if (userMessage.isEmpty()) return;

        Connection connection = DatabaseConnector.openConnection();
        String insertChannel = "INSERT INTO channel (id_message, id_channel,channel_name) VALUES (?,?,?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertChannel)) {

            preparedStatement.setString(1, messageId);
            preparedStatement.setString(2, channelId);
            preparedStatement.setString(3, channelName);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            CommandsHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }

}
