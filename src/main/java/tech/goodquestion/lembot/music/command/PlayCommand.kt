package tech.goodquestion.lembot.music.command

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import tech.goodquestion.lembot.library.Helper

class PlayCommand: ListenerAdapter() {

    @Override
    override fun onMessageReceived(event: MessageReceivedEvent) {

        if (event.message.contentRaw == Helper.PREFIX + "play") {

            event.channel.sendMessage("Lem").queue()
        }

        print("Hello---------------------------------------------")



    }
}