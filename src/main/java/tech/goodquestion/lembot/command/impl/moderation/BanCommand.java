package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import tech.goodquestion.lembot.database.CommandsHelper;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;

public class BanCommand extends UserBanishCommand {


    @Override
    public void banishUser(Member toBanish, Sanction sanction, Message originMsg) {


        sendSanctionReason(toBanish.getUser(), SanctionType.GEBANNT, sanction.reason, toBanish.getAsMention());


        toBanish.ban(0, sanction.reason).complete();

        EmbedBuilder confirmation = new EmbedBuilder();
        confirmation.setColor(0x00ff60);
        confirmation.setTitle("Bestätigung");
        confirmation.setDescription("User " + toBanish.getAsMention() + " wurde durch " + originMsg.getAuthor().getAsMention() + "**" + " gebannt." + "**" + "\n Angegebener Grund: " + sanction.reason);
        originMsg.getChannel().sendMessage(confirmation.build()).queue();

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
