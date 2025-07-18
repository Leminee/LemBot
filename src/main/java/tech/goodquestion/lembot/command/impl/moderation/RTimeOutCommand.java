package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

import java.io.IOException;
import java.sql.SQLException;

public class UnTimeOutCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException, SQLException {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
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
