package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entity.UserData;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

public class BumpCountCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final UserData userData = new UserData();
        userData.userId = sender.getIdLong();

        final String embedTitle = "Bumps";
        
        Helper.getAmount(userData, QueryHelper.AMOUNT_BUMPS, QueryHelper.NEXT_HIGHER_USER_AMOUNT_BUMPS);
        Helper.sendAmount(userData, EmbedColorHelper.BUMP, "Bumps", channel, embedTitle);
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