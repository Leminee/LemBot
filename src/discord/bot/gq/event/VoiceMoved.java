package discord.bot.gq.event;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class VoiceMoved extends ListenerAdapter {

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {


        String userMentioned = event.getMember().getAsMention();
        String voiceChannelLeft= event.getChannelLeft().getName();
        String voiceChannelJoined = event.getChannelJoined().getName();
        long voiceChatId = 834521617668374569L;
        Date date = new Date();

        EmbedBuilder moveEmbed= new EmbedBuilder();

        String embedDescription = userMentioned + " ist von " + "**" + voiceChannelLeft + "**" + " in " + "**" +voiceChannelJoined + "**"+ " um " + date.toString().substring(11, 16) + " Uhr **gemovt**.";

        Helper.createEmbed(moveEmbed,"Voice **Moved** ",embedDescription, Color.blue,"https://cdn.discordapp.com/attachments/819694809765380146/880646674366754856/Bildschirmfoto_2021-08-27_um_04.55.07.png");
        Objects.requireNonNull(event.getJDA().getTextChannelById(voiceChatId)).sendMessage(moveEmbed.build()).queue();

    }
}
