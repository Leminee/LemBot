package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;

import java.io.IOException;

public final class UnwarnCommand extends RemovalBanishment implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

    }

    @Override
    public String getName() {
        return "unwarn";
    }

    @Override
    public String getDescription() {
        return "`unwarn <user> <reason>`: Entfernt" + Config.getInstance().getRoleConfig().getWarnRole().getAsMention();
    }


    @Override
    public String getHelpList() {
        return "staff";
    }
}
