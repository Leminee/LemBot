package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class AutoAnswering extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String userName = Objects.requireNonNull(event.getMember()).getAsMention();

        if (event.getMessage().getAuthor().isBot()) {
            return;
        }

        if ((userMessageContent.startsWith("kennt sich wer") || (userMessageContent.contains("kennt sich wer") || userMessageContent.startsWith("kennt sich jemand") || userMessageContent.startsWith("Kennt sich jemand") ||
                userMessageContent.startsWith("kennt sich wer aus mit") || userMessageContent.contains("kann mir jemand helfen")) || userMessageContent.contains("Kennt sich wer mit") ||
                userMessageContent.contains("wer kennt sich aus") && userMessageContent.length() < 20)) {
            event.getChannel().sendMessage("Stelle einfach deine Frage - möglichst detailliert! " + userName).queue();
        }
    }
}
