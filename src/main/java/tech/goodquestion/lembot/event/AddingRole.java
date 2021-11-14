package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AddingRole extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        List<Role> userRoleListAfter1Minute = Objects.requireNonNull(event.getMember()).getRoles();
        Role codingRole = event.getJDA().getRoleById(784773593942327297L);

        final Runnable addRole = () -> {

            for (Role roleAfter1Minute : userRoleListAfter1Minute) {

                if (roleAfter1Minute == null || roleAfter1Minute.getIdLong() == 808779281211719680L || roleAfter1Minute.getIdLong() == 809152859492974692L
                        || roleAfter1Minute.getIdLong() == 846812921375359027L || roleAfter1Minute.getIdLong() == 846856698979418152L ||
                        roleAfter1Minute.getIdLong() == 808768626844893184L || roleAfter1Minute.getIdLong() == 808767910696189975L
                        || roleAfter1Minute.getIdLong() == 808779520286654554L) {

                    assert codingRole != null;
                    event.getMember().getGuild().addRoleToMember(event.getMember(), codingRole).queue();
                }
            }
        };

        scheduler.schedule(addRole, 1, TimeUnit.MINUTES);

    }
}
