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

public class RaidDetection extends ListenerAdapter {

    private boolean isSameAttack = false;

    @Override
    public void onGuildMemberJoin(final @NotNull GuildMemberJoinEvent event) {

        if (isSameAttack) return;

        final Guild guild = event.getGuild();
        final String moderatorRoleAsMention = Config.getInstance().getRole().getModeratorRole().getAsMention();

        if (QueryHelper.isServerUnderAttack()) {

            isSameAttack = true;

            Objects.requireNonNull(guild.getTextChannelById(Config.getInstance().getChannel().getAutoModerationChannel().getIdLong()))
                    .sendMessage(":red_circle: Es findet gerade einen Raid statt " + moderatorRoleAsMention)
                    .queue();
        }

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final Runnable runnable = () -> isSameAttack = false;

        final int delay = 30;
        scheduler.schedule(runnable, delay, TimeUnit.MINUTES);
    }
}