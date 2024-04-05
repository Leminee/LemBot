package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.entities.Message;

public interface RemovalBanishment {

    void removeSanction(Message message, String[] args);

}