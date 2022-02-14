package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.parser.LocalDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.Objects;

public class LinkDeletion extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        final String userMessage = event.getMessage().getContentRaw();

        if (!userMessage.contains("https://") && !userMessage.contains("http://")) return;

        long channelId = event.getChannel().getIdLong();
        Member author = event.getMember();
        assert author != null;
        boolean isStaff = author.hasPermission(Permission.MESSAGE_MANAGE);

        if (channelId != Config.getInstance().getChannelConfig().getNewArrivalsChannel().getIdLong() || isStaff) {
            return;
        }

        event.getMessage().delete().queue();

        final String authorAsMention = event.getAuthor().getAsMention();
        final String channelAsMention = event.getChannel().getAsMention();
        event.getMessage().reply(":x: Nachricht wurde gelöscht, da sie einen Link enthält, der nicht verifiziert werden konnte").queue();

        Objects.requireNonNull(event.getGuild().getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                .sendMessage(":red_circle:  **Folgender Link wurde gelöscht** \n" + userMessage + "\n**(gesendet von " + authorAsMention + " in " + channelAsMention + " am " + LocalDateTimeFormatter.toGermanFormat(LocalDateTime.now()) + ")**")
                .queue();
    }
}
