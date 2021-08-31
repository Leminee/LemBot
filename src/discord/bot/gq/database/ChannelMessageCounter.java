package discord.bot.gq.database;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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

        if (!userMessage.isEmpty()) {

            ConnectionToDB connectionToDB = new ConnectionToDB();
            connectionToDB.initialize();

            String insertChannel = "INSERT INTO channel (id_message, id_channel,channel_name) VALUES (?,?,?);";

            try(PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(insertChannel)) {

                preparedStatement.setString(1, messageId);
                preparedStatement.setString(2, channelId);
                preparedStatement.setString(3, channelName);

                preparedStatement.executeUpdate();

            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());
            }
        }
    }
}
