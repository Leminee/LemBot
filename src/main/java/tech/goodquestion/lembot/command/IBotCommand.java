package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;
import java.sql.SQLException;


public interface IBotCommand {

    void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException, SQLException;

    String getName();

    String getDescription();

    default boolean isPermitted(final Member member) {
        return Helper.isStaff(member);
    }

    default String getHelpList() {
        return "general";
    }
}