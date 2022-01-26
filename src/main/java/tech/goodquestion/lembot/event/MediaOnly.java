package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;

public class MediaOnly extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event){

        final long currentChannelId = event.getChannel().getIdLong();
        final long memeChannelId = Config.getInstance().getChannel().getAutoModerationChannel().getIdLong();

        if (currentChannelId != memeChannelId) return;

        if(!containsMedia(event)) deleteTextMessage(event);


    }

    private boolean containsMedia(final MessageReceivedEvent event){

        return  event.getMessage().getAttachments().size() != 0;

    }
    private void deleteTextMessage(final MessageReceivedEvent event){

        event.getMessage().delete().queue();

    }
}
