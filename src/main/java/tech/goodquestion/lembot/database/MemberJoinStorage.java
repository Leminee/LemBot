package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class MemberJoinStorage extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@SuppressWarnings("null") GuildMemberJoinEvent event) {
        CommandHelper.logUserJoin(event.getUser());
        CommandHelper.logMemberAmount(event.getGuild());
    }
}
