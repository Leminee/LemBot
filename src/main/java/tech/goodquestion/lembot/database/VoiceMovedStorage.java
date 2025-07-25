package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entity.VoiceChannel;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;
import tech.goodquestion.lembot.library.parser.LocalDateTimeFormatter;

import java.time.LocalTime;

public final class VoiceMovedStorage extends ListenerAdapter {

    @Override
    public void onGuildVoiceMove(@SuppressWarnings("null") @NotNull final GuildVoiceMoveEvent event) {

        final String insertQuery = "INSERT INTO voice_move (id_discord, user_tag, username, moved_from) VALUES (?,?,?,?);";

        final String memberAsMention = event.getMember().getAsMention();
        final String voiceChannelLeft = event.getChannelLeft().getName();
        final String voiceChannelJoined = event.getChannelJoined().getName();

        final VoiceChannel voiceChannel = new VoiceChannel();
        voiceChannel.memberId = event.getMember().getIdLong();
        voiceChannel.memberTag = event.getMember().getUser().getAsTag();
        voiceChannel.memberName = event.getMember().getUser().getName();
        voiceChannel.name = event.getChannelLeft().getName();

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        final String embedDescription = memberAsMention + " ist von " + "**" + voiceChannelLeft + "**" + " in " + "**" + voiceChannelJoined + "**" + " um " + LocalDateTimeFormatter.formatTime(LocalTime.now()) + " Uhr **gemoved**.";

        Helper.createEmbed(embedBuilder, "Voice gemoved ", embedDescription, EmbedColorHelper.VOICE_MOVED, "https://cdn.discordapp.com/attachments/819694809765380146/880646674366754856/Bildschirmfoto_2021-08-27_um_04.55.07.png");

        Config.getInstance().getChannelConfig().getVoiceChatChannel().sendMessageEmbeds(embedBuilder.build()).queue();

        CommandHelper.logVoiceChannelData(insertQuery, voiceChannel);
    }
}