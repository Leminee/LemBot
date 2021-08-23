package discord.bot.gq.database;

import discord.bot.gq.lib.Helper;
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

        long userId = event.getMember().getIdLong();
        String userTag = event.getMember().getUser().getAsTag();
        String userName = event.getMember().getUser().getName();
        String userMentioned = event.getMember().getAsMention();
        String voiceChannelName = event.getChannelJoined().getName();
        long voiceChatId = 834521617668374569L;
        Date date = new Date();

        EmbedBuilder embed = new EmbedBuilder();

        String embedDescription = userMentioned + " ist " + "**" +voiceChannelName + "**" + " um " + date.toString().substring(11, 16) + " Uhr gejoint.";

        Helper.createEmbed(embed,"Voice Joined ",embedDescription, Color.ORANGE,"https://cdn.discordapp.com/attachments/819694809765380146/879198267152400425/Bildschirmfoto_2021-08-23_um_04.59.38.png");
        Objects.requireNonNull(event.getJDA().getTextChannelById(voiceChatId)).sendMessage(embed.build()).queue();

        Helper.insertVoiceChannelData(insertQuery,userId,userTag, userName, voiceChannelName);



    }
}
