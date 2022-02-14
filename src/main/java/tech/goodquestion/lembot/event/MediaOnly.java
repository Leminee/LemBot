package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;

import java.util.Set;

public class MediaOnly extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        final long memeChannelId = Config.getInstance().getChannelConfig().getMemesChannel().getIdLong();
        final Set<Long> mediaOnlyChannels = Set.of(
                memeChannelId
        );

        final long currentChannelId = event.getChannel().getIdLong();

        if (mediaOnlyChannels.contains(currentChannelId) && !messageContainsMedia(event)) deleteTextMessage(event);

    }

    private boolean messageContainsMedia(final MessageReceivedEvent event) {

        return event.getMessage().getAttachments().size() != 0;
    }

    private void deleteTextMessage(final MessageReceivedEvent event) {

        event.getMessage().delete().queue();
    }
}
