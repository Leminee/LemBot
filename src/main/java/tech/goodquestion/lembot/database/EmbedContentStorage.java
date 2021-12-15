package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class EmbedContentStorage extends ListenerAdapter {

    @Override
    public void onMessageEmbed(@Nonnull MessageEmbedEvent event) {


        event.getMessageEmbeds().get(0).getAuthor().getName();
        event.getMessageEmbeds().size();
        event.getMessageEmbeds().get(0).getColor();
        event.getMessageEmbeds().get(0).getImage();
        event.getMessageEmbeds().get(0).getLength();
        event.getMessageEmbeds().get(0).getDescription();
        event.getMessageEmbeds().get(0).getFooter();

    }
}
