package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class AddingRole extends ListenerAdapter {

    private final Map<Long, ScheduledFuture<?>> tasks = new HashMap<>();

    @Override
    public void onGuildMemberJoin(@NotNull final GuildMemberJoinEvent event) {

        final Member member = event.getMember();
        final Guild guild = member.getGuild();
        final Role codingRole = Config.getInstance().getRoleConfig().getCodingRole();

        final int delayInDays = 1;
        assert codingRole != null;
        ScheduledFuture<?> task = guild.addRoleToMember(member, codingRole).queueAfter(delayInDays, TimeUnit.DAYS);
        tasks.put(member.getIdLong(), task);

    }

    @Override
    public void onGuildMemberRoleAdd(final GuildMemberRoleAddEvent event) {

        final Member member = event.getMember();
        final Guild guild = member.getGuild();
        final Role codingRole = Config.getInstance().getRoleConfig().getCodingRole();
        final Role hackingRole = Config.getInstance().getRoleConfig().getHackingRole();
        final Role mutedRole = Config.getInstance().getRoleConfig().getMuteRole();
        final List<Role> memberAddedRoles = member.getRoles();

        for (final Role addedRole : memberAddedRoles) {

            assert codingRole != null;
            assert hackingRole != null;
            assert mutedRole != null;

            final boolean hasAlreadyAccess = addedRole.getIdLong() == codingRole.getIdLong() || addedRole.getIdLong() == hackingRole.getIdLong();
            final boolean isMuted = addedRole.getIdLong() == mutedRole.getIdLong();

            if (hasAlreadyAccess || isMuted) {

                ScheduledFuture<?> task = tasks.remove(member.getIdLong());
                if (task != null) task.cancel(false);
                return;
            }
        }

        assert codingRole != null;
        guild.addRoleToMember(member, codingRole).queue();
        ScheduledFuture<?> task = tasks.remove(member.getIdLong());
        if (task != null) task.cancel(false);
    }
}
