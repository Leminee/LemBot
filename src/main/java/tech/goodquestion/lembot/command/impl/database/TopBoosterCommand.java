package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

import java.io.IOException;

public class TopBoosterCommand implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

    }

    @Override
    public String getName() {
        return "?topbo";
    }

    @Override
    public String getDescription() {
        return "`?topbo`: User mit den meisten Boosts";
    }

    @Override
    public boolean isPermitted(Member member) {
        return IBotCommand.super.isPermitted(member);
    }

    @Override
    public String getHelpList() {
        return IBotCommand.super.getHelpList();
    }
}
