package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;

import java.util.Objects;

public final class RaidDetection extends ListenerAdapter {

    private boolean isSameAttack = false;

    @SuppressWarnings("null")
    @Override
    public void onGuildMemberJoin(@SuppressWarnings("null") final @NotNull GuildMemberJoinEvent event) {

        if (isSameAttack) return;

        final Guild guild = event.getGuild();
        final String moderatorRoleAsMention = Config.getInstance().getRoleConfig().getModeratorRole().getAsMention();
        final String adminRoleAsMention = Config.getInstance().getRoleConfig().getAdminRole().getAsMention();

        if (QueryHelper.isServerUnderAttack()) {

            isSameAttack = true;

            Objects.requireNonNull(guild.getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong())).sendMessage(String.format(":red_circle: Es findet gerade einen Raid statt %s %s ", moderatorRoleAsMention, adminRoleAsMention)).queue();
        }



        /*

        pause invites

        List<String> features = new ArrayList<>(guild.getFeatures());
        features.add("INVITES_DISABLED");

        */


        /*try (ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1)) {

            final Runnable runnable = () -> isSameAttack = false;

            final int delay = 30;
            scheduler.schedule(runnable, delay, TimeUnit.MINUTES);
        }*/
    }
}