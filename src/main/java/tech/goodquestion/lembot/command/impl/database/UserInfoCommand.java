package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

import java.io.IOException;

public class UserInfoCommand implements IBotCommand {
    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {



    }

    @Override
    public String getName() {
        return "ui";
    }

    @Override
    public String getDescription() {
        return "`ui <user>`: Informationen zum User";
    }

    @Override
    public boolean isPermitted(final Member member){
        return true;
    }
}
