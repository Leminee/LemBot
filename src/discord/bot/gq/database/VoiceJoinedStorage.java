package discord.bot.gq.database;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class VoiceJoinedStorage extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        String insertQuery = "INSERT INTO voice_leave (id_discord, user_tag, username, voice_channel_name) VALUES (?,?,?,?);";

        long userId = event.getMember().getIdLong();
        String userTag = event.getMember().getUser().getAsTag();
        String userName = event.getMember().getNickname();
        String voiceChannelName = event.getChannelJoined().getName();

        Helper.insertVoiceChannelData(insertQuery,userId,userTag, userName, voiceChannelName);



    }
}
