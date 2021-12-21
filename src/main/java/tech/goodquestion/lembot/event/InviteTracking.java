package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.entity.InviteData;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class InviteTracking extends ListenerAdapter {

    private final Map<String, InviteData> invitesCache = new ConcurrentHashMap<>();

    @Override
    public void onGuildInviteCreate(final GuildInviteCreateEvent event) {
        final String code = event.getCode();
        final InviteData inviteData = new InviteData(event.getInvite());
        invitesCache.put(code, inviteData);
    }

    @Override
    public void onGuildInviteDelete(final GuildInviteDeleteEvent event) {
        final String code = event.getCode();
        invitesCache.remove(code);
    }

    @Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent event) {

        System.out.println("test");
        final Guild guild = event.getGuild();
        final User user = event.getUser();
        final Member selfMember = guild.getSelfMember();

        if (!selfMember.hasPermission(Permission.MANAGE_SERVER) || user.isBot())
            return;

        guild.retrieveInvites().queue(retrievedInvites ->
        {
            for (final Invite retrievedInvite : retrievedInvites) {
                final String code = retrievedInvite.getCode();
                final InviteData cachedInvite = invitesCache.get(code);
                if (cachedInvite == null)
                    continue;
                if (retrievedInvite.getUses() == cachedInvite.getUses())
                    continue;
                cachedInvite.incrementUses();
                final String pattern = "User %s hat den Invite-Link mit der Url %s genutzt. Dieser wurde durch %s erstellt.";
                final String tag = user.getAsTag();
                final String url = retrievedInvite.getUrl();
                final String inviterTag = Objects.requireNonNull(retrievedInvite.getInviter()).getAsTag();
                final String toLog = String.format(pattern, tag, url, inviterTag);
                System.out.println(toLog);
                break;
            }
        });
    }

    @Override
    public void onGuildReady(final GuildReadyEvent event) {

        final Guild guild = event.getGuild();
        attemptInviteCaching(guild);
    }

    @Override
    public void onGuildJoin(final GuildJoinEvent event) {
        final Guild guild = event.getGuild();
        attemptInviteCaching(guild);
    }

    @Override
    public void onGuildLeave(final GuildLeaveEvent event) {
        final long guildId = event.getGuild().getIdLong();
        invitesCache.entrySet().removeIf(entry -> entry.getValue().getGuildId() == guildId);
    }

    private void attemptInviteCaching(final Guild guild) {
        final Member selfMember = guild.getSelfMember();

        if (!selfMember.hasPermission(Permission.MANAGE_SERVER))
            return;

        guild.retrieveInvites().queue(retrievedInvites ->
                retrievedInvites.forEach(retrievedInvite ->
                        invitesCache.put(retrievedInvite.getCode(), new InviteData(retrievedInvite))));
    }
}
