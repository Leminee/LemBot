package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

public class MetaQuestionCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        channel.sendMessage("Stelle bitte keine Metafrage, stelle einfach deine Frage - möglichst detailliert!").queue();
    }

    @Override
    public String getName() {
        return "mq";
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
