package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;

public class UserSanctionHistory implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

        if (args.length != 1) return;

        User user = Helper.getUserFromCommandInput(message, args);

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getAsTag(),null,user.getEffectiveAvatarUrl());
        Helper.createEmbed(embedBuilder, "Historie der Sanktionen", QueryHelper.getSanctionHistoryBy(user.getIdLong()), EmbedColorHelper.MEMBER_INFO);
        Helper.sendEmbed(embedBuilder,message, true);
    }

    @Override
    public String getName() {
        return "ush";
    }

    @Override
    public String getDescription() {
        return "`user <user>`: Historie der Sanktionen";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
