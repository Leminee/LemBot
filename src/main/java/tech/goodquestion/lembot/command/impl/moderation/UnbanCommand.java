package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

import java.io.IOException;

public final class UnbanCommand extends RemovalBanishment implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {



    }

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getDescription() {
        return "`unban <user> <reason>`: Entfernt den Ban";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
