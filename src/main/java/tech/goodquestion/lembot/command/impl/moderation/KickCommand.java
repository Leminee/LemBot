package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entities.Sanction;

public class KickCommand extends UserBanishCommand {

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "`?kick <user> <reason>`: kickt den user";
    }

    @Override
    public void banishUser(Member toBanish, Sanction sanction, Message originMsg) {
        sendSanctionReason(toBanish.getUser(), "gekickt", sanction.reason, toBanish.getAsMention());
        toBanish.kick(sanction.reason).complete();

        EmbedBuilder confirmation = new EmbedBuilder();
        confirmation.setColor(0x00ff60);
        confirmation.setTitle("Bestätigung");
        confirmation.setDescription("User " + toBanish.getAsMention() + " wurde durch " + originMsg.getAuthor().getAsMention()
                + "**" + " gekickt." + "**" + "\n Angegebener Grund: " + sanction.reason);
        originMsg.getChannel().sendMessage(confirmation.build()).queue();

        QueryHelper.logUserKick(sanction);
    }

    @Override
    public boolean requiresAdmin() {
        return true;
    }
}
