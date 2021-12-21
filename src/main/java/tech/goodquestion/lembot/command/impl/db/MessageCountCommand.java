package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entity.UserData;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

public class MessageCountCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        UserData userData = new UserData();
        userData.userId = sender.getIdLong();

        String embedTitle = "Nachrichten";

        Helper.getAmount(userData, QueryHelper.AMOUNT_MESSAGES, QueryHelper.NEXT_HIGHER_USER_AMOUNT_MESSAGES);
        Helper.sendAmount(userData, EmbedColorHelper.FLOOD, "Nachrichten", channel, embedTitle);
    }

    @Override
    public String getName() {
        return "hmm";
    }

    @Override
    public String getDescription() {
        return "`?hmm`: Anzahl deiner geschickten Nachrichten";
    }
}
