package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

import java.io.IOException;

public class AdvertisingCommand implements IBotCommand {


    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

    }

    @Override
    public String getName() {
        return "?ad";
    }

    @Override
    public String getDescription() {
        return "`ad <user> <delay>`: Schickt dem User Werbung";
    }

    @Override
    public String getHelpList(){
        return "staff";
    }

}
