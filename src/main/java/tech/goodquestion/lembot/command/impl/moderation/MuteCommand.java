package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

import java.io.IOException;

public class MuteCommand implements IBotCommand {


    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {


    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "`mute <user> <dauer> <reason>`: Schickt den User in Timeout";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}