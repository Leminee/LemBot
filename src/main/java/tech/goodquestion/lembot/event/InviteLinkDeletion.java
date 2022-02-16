package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.Helper;

import java.util.Objects;

public final class InviteLinkDeletion extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {

        final String userMessage = event.getMessage().getContentRaw();

        if (!(userMessage.contains("https://discord.gg") || userMessage.contains("https://discord.com/invite/") || userMessage.contains("https://discord.io/")))
            return;

        final long channelId = event.getChannel().getIdLong();
        final Member author = event.getMember();
        assert author != null;
        final boolean isStaff = author.hasPermission(Permission.MESSAGE_MANAGE);

        if (isStaff || channelId == Config.getInstance().getChannelConfig().getYourProjectsChannel().getIdLong()) {
            return;
        }

        event.getMessage().delete().queue();

        final String authorAsMention = event.getAuthor().getAsMention();
        final long logChannelId = Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong();
        final String channelAsMention = event.getChannel().getAsMention();

        event.getMessage().reply(":x: Hier dürfen keine Invitelinks gepostet werden!").queue();

        Objects.requireNonNull(event.getGuild().getTextChannelById(logChannelId))
                .sendMessage(":red_circle:  **Einladungslink gelöscht** \n" + userMessage + "\n**(gesendet von " + authorAsMention + " in " + channelAsMention + " am " + Helper.getGermanDateTime() + ")**")
                .queue();
    }
}
