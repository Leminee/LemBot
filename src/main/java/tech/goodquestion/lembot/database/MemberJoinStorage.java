package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberJoinStorage extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        CommandsHelper.logUserJoin(event.getUser());
        CommandsHelper.logMemberAmount(event.getGuild());

    }
}
