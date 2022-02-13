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

public class HoppingDetection extends ListenerAdapter {

    @Override
    public void onGuildVoiceMove(@NotNull final GuildVoiceMoveEvent event) {


        if (QueryHelper.isHopper(event.getMember().getIdLong())) muteHopper(event.getMember());

    }

    @Override
    public void onGuildVoiceJoin(@NotNull final GuildVoiceJoinEvent event) {


        if (QueryHelper.isHopper(event.getMember().getIdLong())) muteHopper(event.getMember());


    }

    private void muteHopper(final Member member) {

        final int duration = 1;
        member.timeoutFor(Duration.ofMinutes(duration)).queue();

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(member.getUser().getAsTag(),null,member.getEffectiveAvatarUrl());
        embedBuilder.setTitle("Hopping Detection");
        embedBuilder.setDescription("Timeout aufgrund von Hopping");
        embedBuilder.addField("Member", member.getAsMention(),true);
        embedBuilder.addField("Grund", "Hopping",true);
        embedBuilder.addField("Dauer",duration + " Minute",true);
        embedBuilder.addField("Hops", "5 in 30s",false);
        embedBuilder.setColor(Color.decode(EmbedColorHelper.AUTO_MODERATION));
        embedBuilder.setTimestamp(Instant.now());

        Objects.requireNonNull(Config.getInstance()
                .getGuild()
                .getTextChannelById(Config.getInstance().getChannelConfig().getAutoModerationChannel().getIdLong()))
                .sendMessageEmbeds(embedBuilder.build()).queue();
    }

}
