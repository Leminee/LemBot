package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class AddingRole extends ListenerAdapter {

    Map<Long, Future<?>> tasks = new HashMap<>();

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        Member member = event.getMember();
        Guild guild = member.getGuild();
        Role codingRole = guild.getRoleById(784773593942327297L);

        assert codingRole != null;
        int delay = 5;
        Future<?> task = guild.addRoleToMember(member, codingRole).queueAfter(delay, TimeUnit.MINUTES);
        tasks.put(member.getIdLong(), task);
    }

    @Override
    public void onGuildMemberUpdate(GuildMemberUpdateEvent event) {

        Member member = event.getMember();
        Guild guild = member.getGuild();
        long hRoleId = 811741950092116038L;
        long lRole = 808779281211719680L;
        Role codingRole = guild.getRoleById(784773593942327297L);


        if (!member.getRoles().isEmpty()) {
            Future<?> task = tasks.remove(member.getIdLong());
            if (task != null) task.cancel(false);
        }

        if (member.getRoles().contains(lRole) && !member.getRoles().contains(hRoleId)) {
            guild.addRoleToMember(member,codingRole).queue();
        }
    }
}
