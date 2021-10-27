package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entities.Sanction;

public class WarnCommand extends UserBanishCommand {

    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "`?warn` <user> <reason>: Weist dem <user> <@&879448018372395048> zu (Verwarnung)";
    }

    @Override
    public void banishUser(Member toBanish, Sanction sanction, Message originMsg) {

        toBanish.getGuild().addRoleToMember(sanction.userId, Config.getInstance().getRoles().getWarnRole()).queue();

        sendSanctionReason(toBanish.getUser(), "verwarnt", sanction.reason, toBanish.getAsMention());

        EmbedBuilder confirmation = new EmbedBuilder();
        confirmation.setColor(0x00ff60);
        confirmation.setTitle("Bestätigung");
        confirmation.setDescription("User " + toBanish.getAsMention() + " wurde durch " + originMsg.getAuthor().getAsMention() +
                "**" + " verwarnt." + "**" + "\n Angegebener Grund: " + sanction.reason);
        originMsg.getChannel().sendMessage(confirmation.build()).queue();

        QueryHelper.logUserWarn(sanction);
    }

    @Override
    public boolean requiresAdmin() {
        return false;
    }
}
