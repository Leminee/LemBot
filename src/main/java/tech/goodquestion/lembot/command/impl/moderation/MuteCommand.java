package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entities.Sanction;

import java.util.List;
import java.util.Objects;

public class MuteCommand extends UserBanishCommand {

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "`?mute <user> <reason>`: weist dem User " + Config.getInstance().getRoles().getMuteRole().getAsMention() + " zu (Mute)";
    }

    @Override
    public void banishUser(Member toBanish, Sanction sanction, Message originMsg) {
        List<Role> userRoleList = Objects.requireNonNull(toBanish).getRoles();

        for (Role role : userRoleList) {
            toBanish.getGuild().removeRoleFromMember(sanction.userId, role).queue();
        }

        toBanish.getGuild().addRoleToMember(sanction.userId, Config.getInstance().getRoles().getMuteRole()).queue();

        sendSanctionReason(toBanish.getUser(), "gemutet", sanction.reason, toBanish.getAsMention());

        EmbedBuilder confirmation = new EmbedBuilder();
        confirmation.setColor(0x00ff60);
        confirmation.setTitle("Bestätigung");
        confirmation.setDescription("User " + toBanish.getAsMention() + " wurde durch " + originMsg.getAuthor().getAsMention()
                + "**" + " gemutet." + "**" + "\n Angegebener Grund: " + sanction.reason);
        originMsg.getChannel().sendMessage(confirmation.build()).queue();

        QueryHelper.logUserMute(sanction);
    }

    @Override
    public boolean requiresAdmin() {
        return false;
    }
}
