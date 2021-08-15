package discord.bot.gq.database;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class VoiceLeavedStorage extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {

        String insertQuery = "INSERT INTO voice_join (id_discord, user_tag, username, voice_channel_name) VALUES (?,?,?,?);";

        long userId = event.getMember().getIdLong();
        String userTag = event.getMember().getUser().getAsTag();
        String userName = event.getMember().getNickname();
        String voiceChannelName = event.getChannelLeft().getName();

        Helper.insertVoiceChannelData(insertQuery,userId,userTag, userName, voiceChannelName);


    }
}
