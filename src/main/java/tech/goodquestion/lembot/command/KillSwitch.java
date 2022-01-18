package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;

public class KillSwitch implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

        final boolean hasPermission = sender.hasPermission(Permission.ADMINISTRATOR);

        if (!hasPermission) return;

            System.exit(0);
    }

    @Override
    public String getName() {
        return "kill";
    }

    @Override
    public String getDescription() {
        return null;
    }

}
