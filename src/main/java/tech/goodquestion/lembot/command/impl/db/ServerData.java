package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

public class ServerData implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {


    }

    @Override
    public String getName() {
        return "?server";
    }

    @Override
    public String getDescription() {
        return "`?server`: Informationen zum Server";
    }
}
