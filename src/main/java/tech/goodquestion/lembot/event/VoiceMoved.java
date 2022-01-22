package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

public class VoiceMoved extends ListenerAdapter {

    @Override
    public void onGuildVoiceMove(@NotNull final GuildVoiceMoveEvent event) {

        final String memberAsMention = event.getMember().getAsMention();
        final String voiceChannelLeft = event.getChannelLeft().getName();
        final String voiceChannelJoined = event.getChannelJoined().getName();

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        final String embedDescription = memberAsMention + " ist von " + "**" + voiceChannelLeft + "**" + " in " + "**" + voiceChannelJoined + "**" + " um " + Helper.getCurrentDateTime() + " Uhr **gemovt**.";

        Helper.createEmbed(embedBuilder,
                "Voice gewechselt ",
                embedDescription,
                EmbedColorHelper.VOICE_MOVED,
                "https://cdn.discordapp.com/attachments/819694809765380146/880646674366754856/Bildschirmfoto_2021-08-27_um_04.55.07.png");

        Config.getInstance().getChannel().getVoiceChatChannel().sendMessageEmbeds(embedBuilder.build()).queue();

    }
}
