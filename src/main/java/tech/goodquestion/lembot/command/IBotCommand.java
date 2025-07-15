package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;
import java.sql.SQLException;


public interface IBotCommand {

    void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException, SQLException;

    String getName();

    String getDescription();

    default boolean isPermitted(final Member member) {
        return member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("administrator")) || member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("moderator"));
    }

    default String getHelpList() {
        return "general";
    }
}