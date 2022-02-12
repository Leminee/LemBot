package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;


public interface IBotCommand {

    void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException;
    String getName();
    String getDescription();

    default boolean isPermitted(final Member member) {
        return member.hasPermission(Permission.MESSAGE_MANAGE);
    }
    default String getHelpList() {
        return "general";
    }
}
