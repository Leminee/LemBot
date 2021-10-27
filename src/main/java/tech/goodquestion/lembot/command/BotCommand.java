package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface BotCommand {
    void dispatch(Message msg, TextChannel channel, Member sender, String[] args);
    String getName();
    String getDescription();
    default boolean isPermitted(Member member) {
        return true;
    }
    default String getHelpList() {
        return "default";
    }
}
