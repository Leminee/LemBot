package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entities.VoiceChannel;
import tech.goodquestion.lembot.lib.Helper;

import java.awt.*;

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

        EmbedBuilder leftEmbed = new EmbedBuilder();

        String embedDescription = userMentioned + " hat " + "**" + voiceChannel.name + "**" + " um " + Helper.getCurrentTime() + " Uhr **verlassen**.";

        Helper.createEmbed(leftEmbed,"Voice **Left** ",embedDescription, Color.MAGENTA,"https://cdn.discordapp.com/attachments/819694809765380146/880646674366754856/Bildschirmfoto_2021-08-27_um_04.55.07.png");
        Config.getInstance().getChannels().getVoiceChatChannel().sendMessage(leftEmbed.build()).queue();

        Helper.insertVoiceChannelData(insertQuery,voiceChannel);
    }

}
