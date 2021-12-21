package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ReactionStorage extends ListenerAdapter {

    @Override
   public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {

        final long messageId = event.getMessageIdLong();
        final long userId = Objects.requireNonNull(event.getMember()).getUser().getIdLong();

        try {
            final String addedReaction = event.getReaction().getReactionEmote().getEmoji();
            CommandsHelper.logUserReaction(messageId,userId,addedReaction);

        }catch (IllegalStateException illegalStateException) {

            CommandsHelper.logUserReaction(messageId,userId,null);
        }
    }


    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {

    }

}
