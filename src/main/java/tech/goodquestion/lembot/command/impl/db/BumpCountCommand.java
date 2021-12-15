package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.entity.UserData;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

public class BumpCountCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        UserData userData = new UserData();
        userData.userId = sender.getIdLong();

        String amountBumps = "SELECT number_bumps FROM user_bump WHERE id_discord = ?";
        String nextHigherUserAmountBumps = "SELECT id_discord, number_bumps FROM user_bump WHERE number_bumps > ? ORDER BY number_bumps, username LIMIT 1";

        Helper.getAmount(userData, amountBumps, nextHigherUserAmountBumps);
        Helper.sendAmount(userData, EmbedColorHelper.BUMP, "Bumps", channel);
    }

    @Override
    public String getName() {
        return "hmb";
    }

    @Override
    public String getDescription() {
        return "`?hmb`: Anzahl deiner Bumps";
    }
}
