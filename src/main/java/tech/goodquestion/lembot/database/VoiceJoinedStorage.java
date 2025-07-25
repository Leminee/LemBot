package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entity.VoiceChannel;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;
import tech.goodquestion.lembot.library.parser.LocalDateTimeFormatter;

import java.time.LocalTime;

public final class VoiceJoinedStorage extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@SuppressWarnings("null") @NotNull final GuildVoiceJoinEvent event) {

        final String insertQuery = "INSERT INTO voice_join (id_discord, user_tag, username, name) VALUES (?,?,?,?);";
        final VoiceChannel voiceChannel = new VoiceChannel();

        voiceChannel.memberId = event.getMember().getIdLong();
        voiceChannel.memberTag = event.getMember().getUser().getAsTag();
        voiceChannel.memberName = event.getMember().getUser().getName();
        voiceChannel.name = event.getChannelJoined().getName();

        final String userMentioned = event.getMember().getAsMention();

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        final String embedDescription = userMentioned + " ist **" + voiceChannel.name + "** um " + LocalDateTimeFormatter.formatTime(LocalTime.now()) + " Uhr **gejoined**.";

        Helper.createEmbed(embedBuilder, "Voice gejoined", embedDescription, EmbedColorHelper.VOICE_JOINED, "https://cdn.discordapp.com/attachments/819694809765380146/880646674366754856/Bildschirmfoto_2021-08-27_um_04.55.07.png");
        Config.getInstance().getChannelConfig().getVoiceChatChannel().sendMessageEmbeds(embedBuilder.build()).queue();

        CommandHelper.logVoiceChannelData(insertQuery, voiceChannel);
    }
}