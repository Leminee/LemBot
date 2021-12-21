package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandsHelper;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;

import java.util.List;
import java.util.Objects;

public class MuteCommand extends UserBanishCommand {


    @Override
    public void banishUser(Member toBanish, Sanction sanction, Message originMsg) {

        List<Role> userRoles = Objects.requireNonNull(toBanish).getRoles();

        for (Role role : userRoles) {

            toBanish.getGuild().removeRoleFromMember(sanction.userId, role).queue();
        }

        toBanish.getGuild().addRoleToMember(sanction.userId, Config.getInstance().getRole().getMuteRole()).queue();

        sendSanctionReason(toBanish.getUser(), SanctionType.GEMUTET, sanction.reason, toBanish.getAsMention());

        EmbedBuilder confirmation = new EmbedBuilder();
        confirmation.setColor(0x00ff60);
        confirmation.setTitle("Bestätigung");
        confirmation.setDescription("User " + toBanish.getAsMention() + " wurde durch " + originMsg.getAuthor().getAsMention()
                + "**" + " gemutet." + "**" + "\n Angegebener Grund: " + sanction.reason);
        originMsg.getChannel().sendMessage(confirmation.build()).queue();

        CommandsHelper.logUserMute(sanction);
    }

    @Override
    public boolean requiresAdmin() {
        return false;
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "`?mute <user> <reason>`: Weist " + Config.getInstance().getRole().getMuteRole().getAsMention() + " zu";
    }
}
