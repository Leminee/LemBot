package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class HoppingDetection extends ListenerAdapter {

    @Override
    public void onGuildVoiceMove(@SuppressWarnings("null") @NotNull final GuildVoiceMoveEvent event) {

        if (QueryHelper.isHopper(event.getMember().getIdLong())) timeOutHopper(event.getMember());
    }

    @Override
    public void onGuildVoiceJoin(@SuppressWarnings("null") @NotNull final GuildVoiceJoinEvent event) {

        if (QueryHelper.isHopper(event.getMember().getIdLong())) timeOutHopper(event.getMember());
    }

    @SuppressWarnings("null")
    private void timeOutHopper(final Member member) {

        final int durationInMinutes = 1;
        member.timeoutFor(Duration.ofMinutes(durationInMinutes)).queue();

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl());
        embedBuilder.setTitle("Hopping Detection");
        embedBuilder.setDescription("Timeout aufgrund von Hopping");
        embedBuilder.addField("Member", member.getAsMention(), true);
        embedBuilder.addField("Dauer", durationInMinutes + " Minute", true);
        embedBuilder.addField("Hops", "10 in 60s", true);
        embedBuilder.addField("Grund", "```Hopping```", false);
        embedBuilder.setColor(Color.decode(EmbedColorHelper.AUTO_MODERATION));
        embedBuilder.setTimestamp(Instant.now());

        Objects.requireNonNull(Config.getInstance()
                .getGuild()
                .getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                .sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
