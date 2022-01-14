package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.lib.Helper;

import java.util.Objects;

public class InviteLinkDeletion extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {

       final String userMessage = event.getMessage().getContentRaw();

        if (!(userMessage.contains("https://discord.gg") || userMessage.contains("https://discord.com/invite/") || userMessage.contains("https://discord.io/"))) return;

        final long authorId = event.getAuthor().getIdLong();
        final long channelId = event.getChannel().getIdLong();
        final long ownerId = Config.getInstance().getGuild().getOwnerIdLong();
        final Member author = event.getMember();
        assert author != null;
        final boolean isStaff = author.hasPermission(Permission.MESSAGE_MANAGE);

        if (authorId == ownerId || isStaff || channelId == Config.getInstance().getChannel().getYourProjectsChannel().getIdLong()) {
            return;
        }

        event.getMessage().delete().queue();

        final String authorAsMention =  event.getAuthor().getAsMention();
        final long logChannelId = Config.getInstance().getChannel().getLogChannel().getIdLong();
        final String channelAsMention = event.getChannel().getAsMention();
        event.getChannel().sendMessage(":x: Hier dürfen keine Invitelinks gepostet werden " + authorAsMention + "!").queue();
        Objects.requireNonNull(event.getGuild().getTextChannelById(logChannelId))
                .sendMessage(":red_circle:  Einladungslink gelöscht " + userMessage + "\n(gesendet von " + authorAsMention + " in " + channelAsMention + " am " + Helper.getGermanDateTime() +")")
                .queue();
    }
}
