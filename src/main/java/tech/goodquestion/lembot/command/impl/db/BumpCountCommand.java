package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.BotCommand;
import tech.goodquestion.lembot.entities.UserData;
import tech.goodquestion.lembot.lib.Helper;

public class BumpCountCommand implements BotCommand {

    @Override
    public void dispatch(Message msg, TextChannel channel, Member sender, String[] args) {
        UserData userData = new UserData();
        userData.userId = sender.getIdLong();

        String amountBumps = "SELECT number_bumps FROM user_bump WHERE id_discord = ?";
        String nextHigherUserAmountBumps = "SELECT id_discord, number_bumps FROM user_bump WHERE number_bumps > ? ORDER BY number_bumps, username LIMIT 1";
        String embedColor = "0x26b7b8";

        Helper.getAmount(userData, amountBumps, nextHigherUserAmountBumps);
        Helper.sendAmount(userData, embedColor, "Bumps", channel);
    }

    @Override
    public String getName() {
        return "hmb";
    }

    @Override
    public String getDescription() {
        return "`?hmb`: Zeigt an, wie viele Bumps du bereits hast";
    }
}
