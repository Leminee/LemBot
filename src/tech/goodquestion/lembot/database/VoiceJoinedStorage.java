package tech.goodquestion.lembot.database;

import tech.goodquestion.lembot.lib.Helper;
import tech.goodquestion.lembot.entities.VoiceChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class VoiceJoinedStorage extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        String insertQuery = "INSERT INTO voice_join (id_discord, user_tag, username, voice_channel_name) VALUES (?,?,?,?);";

        VoiceChannel voiceChannel = new VoiceChannel();

        voiceChannel.userId = event.getMember().getIdLong();
        voiceChannel.userTag = event.getMember().getUser().getAsTag();
        voiceChannel.userName = event.getMember().getUser().getName();
        voiceChannel.name = event.getChannelJoined().getName();

        String userMentioned = event.getMember().getAsMention();
        long voiceChatId = 834521617668374569L;
        Date date = new Date();

        EmbedBuilder joinEmbed = new EmbedBuilder();

        String embedDescription = userMentioned + " ist " + "**" + voiceChannel.name + "**" + " um " + date.toString().substring(11, 16) + " Uhr **gejoint**.";

        Helper.createEmbed(joinEmbed, "Voice **Joined** ", embedDescription, Color.ORANGE, "https://cdn.discordapp.com/attachments/819694809765380146/880646674366754856/Bildschirmfoto_2021-08-27_um_04.55.07.png");
        Objects.requireNonNull(event.getJDA().getTextChannelById(voiceChatId)).sendMessage(joinEmbed.build()).queue();

        Helper.insertVoiceChannelData(insertQuery, voiceChannel);


    }
}
