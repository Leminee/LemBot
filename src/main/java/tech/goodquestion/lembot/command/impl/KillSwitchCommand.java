package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;

import java.io.IOException;
import java.util.Objects;

public final class KillSwitchCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {

        Objects.requireNonNull(Config.getInstance()
                .getGuild()
                .getTextChannelById(Config.getInstance().getChannelConfig().getStaffRoomChannel().getIdLong()))
                .sendMessage(":warning: Bot wurde durch " + sender.getAsMention() + " gekillt!")
                .queue();

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
