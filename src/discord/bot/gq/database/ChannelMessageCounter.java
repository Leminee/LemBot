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

            try {

                ConnectionToDB db = new ConnectionToDB();
                db.initialize();

                String insertChannel = "INSERT INTO channel (id_message, id_channel,channel_name) VALUES (?,?,?);";

                PreparedStatement pS = db.getConnection().prepareStatement(insertChannel);
                pS.setString(1, messageId);
                pS.setString(2, channelId);
                pS.setString(3, channelName);

                pS.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
