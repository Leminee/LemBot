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
    public void onGuildVoiceMove(@NotNull final GuildVoiceMoveEvent event) {

        final String userMentioned = event.getMember().getAsMention();
        final String voiceChannelLeft = event.getChannelLeft().getName();
        final String voiceChannelJoined = event.getChannelJoined().getName();

        final EmbedBuilder moveEmbed = new EmbedBuilder();

        final String embedDescription = userMentioned + " ist von " + "**" + voiceChannelLeft + "**" + " in " + "**" + voiceChannelJoined + "**" + " um " + Helper.getCurrentDateTime() + " Uhr **gemovt**.";

        Helper.createEmbed(moveEmbed, "Voice gewechselt ", embedDescription, EmbedColorHelper.VOICE_MOVED, "https://cdn.discordapp.com/attachments/819694809765380146/880646674366754856/Bildschirmfoto_2021-08-27_um_04.55.07.png");
        Config.getInstance().getChannel().getVoiceChatChannel().sendMessage(moveEmbed.build()).queue();

    }
}
