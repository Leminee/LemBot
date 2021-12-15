package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ReactionStorage extends ListenerAdapter {

    @Override
   public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {

        long messageId = event.getMessageIdLong();
        long userId = Objects.requireNonNull(event.getMember()).getUser().getIdLong();
        String addedReaction = event.getReaction().getReactionEmote().getEmoji();
        int count = event.getReaction().getCount();

        CommandsHelper.logUserReactions(messageId,userId,String.valueOf(addedReaction),count);

    }


    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {}

}
