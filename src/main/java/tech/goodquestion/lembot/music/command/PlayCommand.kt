package tech.goodquestion.lembot.music.command

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.hooks.ListenerAdapter
import tech.goodquestion.lembot.command.IBotCommand

class PlayCommand: ListenerAdapter(), IBotCommand {


    override fun dispatch(message: Message?, channel: TextChannel?, sender: Member?, args: Array<out String>?) {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        return "play"
    }

    override fun getDescription(): String {
        return "`?play <youtube url>`: Spielt den Song ab"
    }
}