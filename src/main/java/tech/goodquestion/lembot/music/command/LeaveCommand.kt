package tech.goodquestion.lembot.music.command

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.managers.AudioManager
import tech.goodquestion.lembot.command.IBotCommand
import tech.goodquestion.lembot.config.Config

class LeaveCommand : IBotCommand {
    override fun dispatch(message: Message?, channel: TextChannel?, sender: Member?, args: Array<out String>?) {

        val audioManager: AudioManager = Config.getInstance().guild.audioManager

        if(audioManager.isConnected) audioManager.closeAudioConnection()

    }

    override fun getName(): String {
       return "leave"
    }

    override fun getDescription(): String {
        return "`leave`: Bot verlässt den Voice"
    }

    override fun isPermitted(member: Member?): Boolean {
        return true
    }

    override fun getHelpList():String {
        return "music"
    }
}