package tech.goodquestion.lembot.music.command

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.managers.AudioManager
import tech.goodquestion.lembot.command.IBotCommand
import tech.goodquestion.lembot.config.Config
import tech.goodquestion.lembot.library.EmbedColorHelper
import tech.goodquestion.lembot.library.Helper

class PlayCommand:IBotCommand {

    override fun dispatch(message: Message?, channel: TextChannel?, sender: Member?, args: Array<out String>?) {

        val memberAsMention: String? = sender?.asMention

        if (args?.size!! == 0)  {

            val embedBuilder = EmbedBuilder()

            Helper.createEmbed(embedBuilder,"Error",":x: Gib bitte die Url an $memberAsMention", EmbedColorHelper.ERROR)

            Helper.sendEmbed(embedBuilder,message,true)

            return

        }

        if (args.size == 1 && args[0].startsWith("https://www.youtube.com").not()) {


            val embedBuilder = EmbedBuilder()

            Helper.createEmbed(embedBuilder,"Error",":x: Url ist nicht valid $memberAsMention", EmbedColorHelper.ERROR)

            Helper.sendEmbed(embedBuilder,message,true)

            return

        }


        if (sender?.voiceState?.inAudioChannel()?.not() == true) {

            val embedBuilder = EmbedBuilder()

            Helper.createEmbed(embedBuilder,"Error",":x: Du musst in einem Voice Channel sein $memberAsMention", EmbedColorHelper.ERROR)

            channel?.sendMessageEmbeds(embedBuilder.build())?.queue()

            return

        }

        val connectedVoiceChannel: VoiceChannel = sender?.voiceState?.channel as VoiceChannel

        val audioManager: AudioManager = Config.getInstance().guild.audioManager

        if(audioManager.isConnected) {

            val embedBuilder = EmbedBuilder()

            Helper.createEmbed(embedBuilder,"Error",":x: Ich bin bereits in einem Voice $memberAsMention", EmbedColorHelper.ERROR)

            channel?.sendMessageEmbeds(embedBuilder.build())?.queue()

            return
        }

        audioManager.openAudioConnection(connectedVoiceChannel)


    }

    override fun getName(): String {
        return "play"
    }

    override fun getDescription(): String {
        return "`play <youtube url>`: Spielt den Song ab"
    }

    override fun isPermitted(member: Member?): Boolean {
        return true
    }


    override fun getHelpList():String {
        return "music"
    }
}