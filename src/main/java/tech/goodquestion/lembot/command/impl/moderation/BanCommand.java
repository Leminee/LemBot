package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import tech.goodquestion.lembot.database.CommandsHelper;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;
import tech.goodquestion.lembot.lib.EmbedColorHelper;

import java.awt.*;

public class BanCommand extends UserBanishCommand {


    @Override
    public void banishUser(Member toBanish, Sanction sanction, Message originMessage) {

        final String performedSanction = "gebannt";
        final SanctionType sanctionType = SanctionType.BAN;
        sendSanctionReason(toBanish.getUser(),sanctionType, performedSanction, sanction.reason, "");

        toBanish.ban(0, sanction.reason).complete();

        final EmbedBuilder confirmation = new EmbedBuilder();
        confirmation.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        confirmation.setTitle("Bestätigung");
        confirmation.setDescription("User " + toBanish.getAsMention() + " wurde durch " + originMessage.getAuthor().getAsMention() + "**" + " gebannt." + "**" + "\n Angegebener Grund: " + sanction.reason);


        originMessage.getChannel().sendMessage(confirmation.build()).queue();

        CommandsHelper.logUserBan(sanction);

    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "`?ban <user> <reason>`: Bannt den User";
    }

    @Override
    public boolean requiresAdmin() {
        return true;
    }
}
