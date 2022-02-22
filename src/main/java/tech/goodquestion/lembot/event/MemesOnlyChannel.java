package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MemesOnlyChannel extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        final long memeChannelId = Config.getInstance().getChannelConfig().getMemesChannel().getIdLong();
        final String messageContent = event.getMessage().getContentRaw();
        final Message message = event.getMessage();
        final Set<Long> memesOnlyChannels = Set.of(
                memeChannelId
        );

        final long currentChannelId = event.getChannel().getIdLong();

        if (!memesOnlyChannels.contains(currentChannelId)) return;

        if (!containsMedia(message) && !containsOnlyURL(messageContent)) deleteTextMessage(message);

        event.getMessage()
                .reply("In diesem Kanal dürfen nur Memes (als Media-Dateien oder Links) gepostet werden")
                .queue(m -> m.delete().queueAfter(30, TimeUnit.SECONDS));

    }

    private boolean containsMedia(final Message message) {

        return message.getAttachments().size() != 0;
    }

    private boolean containsOnlyURL(final String messageContent) {

        Pattern pattern = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Matcher matcher = pattern.matcher(messageContent);

        return matcher.matches();
    }

    private void deleteTextMessage(final Message message) {

        message.delete().queue();
    }
}
