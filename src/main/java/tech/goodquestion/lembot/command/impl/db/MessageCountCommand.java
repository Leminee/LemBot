package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.entities.UserData;
import tech.goodquestion.lembot.lib.Helper;

public class MessageCountCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        UserData userData = new UserData();
        userData.userId = sender.getIdLong();

        String amountMessages = "SELECT number_message FROM user_message WHERE id_discord = ?";
        String nextHigherUserAmountMessages = "SELECT id_discord, number_message FROM user_message WHERE number_message > ? ORDER BY number_message, username LIMIT 1";
        String embedColor = "0xffffff";

        Helper.getAmount(userData, amountMessages, nextHigherUserAmountMessages);
        Helper.sendAmount(userData, embedColor, "Nachrichten", channel);
    }

    @Override
    public String getName() {
        return "hmm";
    }

    @Override
    public String getDescription() {
        return "`?hmm`: Zeigt an, wie viele Nachrichten du auf diesem Server gesendet hast";
    }
}
