package tech.goodquestion.lembot.music.command

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import tech.goodquestion.lembot.command.IBotCommand

class LeaveCommand : IBotCommand {
    override fun dispatch(message: Message?, channel: TextChannel?, sender: Member?, args: Array<out String>?) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
       return "leave"
    }

    override fun getDescription(): String {
        return "`?leave`: Bot verlässt den Voice"
    }

    override fun isPermitted(member: Member?): Boolean {
        return true
    }

    override fun getHelpList():String {
        return "music"
    }
}