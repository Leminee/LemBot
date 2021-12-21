package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandsHelper;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;

public class WarnCommand extends UserBanishCommand {

    @Override
    public void banishUser(Member toBanish, Sanction sanction, Message originMsg) {

        toBanish.getGuild().addRoleToMember(sanction.userId, Config.getInstance().getRole().getWarnRole()).queue();

        sendSanctionReason(toBanish.getUser(), SanctionType.VERWARNT, sanction.reason, toBanish.getAsMention());

        EmbedBuilder confirmation = new EmbedBuilder();
        confirmation.setColor(0x00ff60);
        confirmation.setTitle("Bestätigung");
        confirmation.setDescription("User " + toBanish.getAsMention() + " wurde durch " + originMsg.getAuthor().getAsMention() +
                "**" + " verwarnt." + "**" + "\n Angegebener Grund: " + sanction.reason);
        originMsg.getChannel().sendMessage(confirmation.build()).queue();

        CommandsHelper.logUserWarn(sanction);
    }

    @Override
    public boolean requiresAdmin() {
        return false;
    }

    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "`?warn <user> <reason>:` Weist " + Config.getInstance().getRole().getWarnRole().getAsMention() +" zu";
    }
}
