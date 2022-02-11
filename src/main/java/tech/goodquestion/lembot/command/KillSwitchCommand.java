package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;

public class KillSwitchCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {


        System.exit(0);
    }

    @Override
    public String getName() {
        return "ks";
    }

    @Override
    public String getDescription() {
        return "`ks`: Killt den Bot";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
