package discord.bot.gq.database;

import discord.bot.gq.lib.Helper;
import discord.bot.gq.entities.VoiceChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class VoiceLeftStorage extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {

        String insertQuery = "INSERT INTO voice_leave (id_discord, user_tag, username, voice_channel_name) VALUES (?,?,?,?);";

        VoiceChannel voiceChannel = new VoiceChannel();
        voiceChannel.userId = event.getMember().getIdLong();
        voiceChannel.userTag = event.getMember().getUser().getAsTag();
        voiceChannel.userName = event.getMember().getUser().getName();
        voiceChannel.name = event.getChannelLeft().getName();
        String userMentioned = event.getMember().getAsMention();

        long voiceChatId = 834521617668374569L;
        Date date = new Date();

        EmbedBuilder embed = new EmbedBuilder();

        String embedDescription = userMentioned + " hat " + "**" + voiceChannel.name + "**" + " um " + date.toString().substring(11, 16) + " Uhr **verlassen**.";

        Helper.createEmbed(embed,"Voice **Left** ",embedDescription, Color.MAGENTA,"https://cdn.discordapp.com/attachments/819694809765380146/880646674366754856/Bildschirmfoto_2021-08-27_um_04.55.07.png");
        Objects.requireNonNull(event.getJDA().getTextChannelById(voiceChatId)).sendMessage(embed.build()).queue();

        Helper.insertVoiceChannelData(insertQuery,voiceChannel);


    }
}
