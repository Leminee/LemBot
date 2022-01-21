package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.entity.VoiceChannel;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

public class VoiceLeftStorage extends ListenerAdapter {

    @Override
    public void onGuildVoiceLeave(@NotNull final GuildVoiceLeaveEvent event) {

        final String insertQuery = "INSERT INTO voice_leave (id_discord, user_tag, username, name) VALUES (?,?,?,?);";

        final VoiceChannel voiceChannel = new VoiceChannel();
        voiceChannel.userId = event.getMember().getIdLong();
        voiceChannel.userTag = event.getMember().getUser().getAsTag();
        voiceChannel.userName = event.getMember().getUser().getName();
        voiceChannel.name = event.getChannelLeft().getName();

        final String userMentioned = event.getMember().getAsMention();

        final EmbedBuilder embedBuilder= new EmbedBuilder();

        final String embedDescription = userMentioned + " hat " + "**" + voiceChannel.name + "**" + " um " + Helper.getCurrentDateTime() + " Uhr **verlassen**.";

        Helper.createEmbed(embedBuilder,"Voice verlassen ",embedDescription, EmbedColorHelper.VOICE_LEFT, "https://cdn.discordapp.com/attachments/819694809765380146/880646674366754856/Bildschirmfoto_2021-08-27_um_04.55.07.png");
        Config.getInstance().getChannel().getVoiceChatChannel().sendMessageEmbeds(embedBuilder.build()).queue();

        CommandHelper.insertVoiceChannelData(insertQuery,voiceChannel);
    }

}
