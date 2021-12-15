package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

public class VoiceMoved extends ListenerAdapter {

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {

        String userMentioned = event.getMember().getAsMention();
        String voiceChannelLeft = event.getChannelLeft().getName();
        String voiceChannelJoined = event.getChannelJoined().getName();

        EmbedBuilder moveEmbed = new EmbedBuilder();

        String embedDescription = userMentioned + " ist von " + "**" + voiceChannelLeft + "**" + " in " + "**" +voiceChannelJoined + "**"+ " um " + Helper.getCurrentTime() + " Uhr **gemovt**.";

        Helper.createEmbed(moveEmbed,"Voice **Moved** ",embedDescription, EmbedColorHelper.VOICE_MOVED);
        Config.getInstance().getChannel().getVoiceChatChannel().sendMessage(moveEmbed.build()).queue();

    }
}
