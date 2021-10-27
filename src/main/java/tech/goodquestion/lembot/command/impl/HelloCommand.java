package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

public class HelloCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        channel.sendTyping().queue();
        channel.sendMessage("Hi, Wie geht's dir? " + message.getAuthor().getAsMention() + " Was hast du heute schon gemacht? und was wirst du heute noch tun?").queue();
    }

    @Override
    public String getName() {
        return "hallo";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getHelpList() {
        return null;
    }
}
