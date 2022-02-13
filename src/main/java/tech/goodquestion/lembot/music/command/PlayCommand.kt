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


        if (args?.size!! == 0)  {

            val embedBuilder = EmbedBuilder()

            Helper.createEmbed(embedBuilder,"Fehler",":x: Gib bitte die Url an ", EmbedColorHelper.ERROR)

            Helper.sendEmbed(embedBuilder,message,true)

            return

        }

        if (args.size == 1 && args[0].startsWith("https://www.youtube.com").not()) {


            val embedBuilder = EmbedBuilder()

            Helper.createEmbed(embedBuilder,"Fehler",":x: Url ist nicht valid ", EmbedColorHelper.ERROR)

            Helper.sendEmbed(embedBuilder,message,true)

            return

        }


        if (sender?.voiceState?.inAudioChannel()?.not() == true) {

            val embedBuilder = EmbedBuilder()

            Helper.createEmbed(embedBuilder,"Fehler",":x: Du musst in einem Voice Channel sein ", EmbedColorHelper.ERROR)

            Helper.sendEmbed(embedBuilder,message,true)

            return

        }

        val connectedVoiceChannel: VoiceChannel = sender?.voiceState?.channel as VoiceChannel

        val audioManager: AudioManager = Config.getInstance().guild.audioManager

        if(audioManager.isConnected) {

            val embedBuilder = EmbedBuilder()

            Helper.createEmbed(embedBuilder,"Fehler",":x: Ich bin bereits in einem Voice ", EmbedColorHelper.ERROR)

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