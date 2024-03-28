package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entity.UserData;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;

public class MonthlyBumpsCommand implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

        final UserData userData = new UserData();
        userData.userId = sender.getIdLong();
        final String embedTitle = "Monthly Bumps";

        Helper.getAmount(userData, QueryHelper.AMOUNT_MONTHLY_BUMPS, QueryHelper.NEXT_HIGHER_USER_AMOUNT_MONTHLY_BUMPS);
        Helper.sendAmount(userData, EmbedColorHelper.BUMP, "monatlichen Bumps", message, embedTitle);

    }
    @Override
    public String getName() {
        return "hmmb";
    }

    @Override
    public String getDescription() {
        return "`hmmb`: Anzahl deiner monatlichen Bumps";
    }

    @Override
    public boolean isPermitted(Member member) {
        return true;
    }

    @Override
    public String getHelpList() {
        return "stats";
    }
}
