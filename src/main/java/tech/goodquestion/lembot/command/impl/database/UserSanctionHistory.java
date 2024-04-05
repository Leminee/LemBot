package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.command.impl.AdvertisingCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;

public class UserSanctionHistory implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

        if (args.length != 1) return;

        if (Helper.isStaffChannel(message, channel)) return;

        User user;
        try {

            user = Helper.getUserFromCommandInput(message, args);

        } catch (ErrorResponseException errorResponseException) {

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            Helper.createEmbed(embedBuilder, "Error", ":x: Kein User gefunden", EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl());
        Helper.createEmbed(embedBuilder, "Historie der Sanktionen", QueryHelper.getSanctionHistoryBy(user.getIdLong()), EmbedColorHelper.MEMBER_INFO);
        Helper.sendEmbed(embedBuilder, message, true);
    }

    @Override
    public String getName() {
        return "ush";
    }

    @Override
    public String getDescription() {
        return "`ush <user>`: Historie der Sanktionen";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}