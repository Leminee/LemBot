package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class RaidDetection extends ListenerAdapter {

    private boolean isSameAttack = false;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        if (isSameAttack)
            return;

        final Guild guild = event.getGuild();
        final String moderatorRoleAsMention = Config.getInstance().getRoleConfig().getModeratorRole().getAsMention();
        final String adminRoleAsMention = Config.getInstance().getRoleConfig().getAdminRole().getAsMention();

        if (QueryHelper.isServerUnderAttack()) {

            isSameAttack = true;

            Objects.requireNonNull(guild
                            .getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                    .sendMessage(String.format(":red_circle: Es findet gerade ein Raid statt %s %s",
                            moderatorRoleAsMention, adminRoleAsMention))
                    .queue();

            scheduler.schedule(() -> isSameAttack = false, 30, TimeUnit.MINUTES);
        }
    }
}
