package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;
import tech.goodquestion.lembot.lib.EmbedColorHelper;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class MuteCommand extends UserBanishCommand {


    @Override
    public void banishUser(Member toBanish, Sanction sanction, Message originMsg) {

        final List<Role> userRoles = Objects.requireNonNull(toBanish).getRoles();

        for (Role role : userRoles) {

            toBanish.getGuild().removeRoleFromMember(sanction.userId, role).queue();
        }

        toBanish.getGuild().addRoleToMember(sanction.userId, Config.getInstance().getRole().getMuteRole()).queue();

        if (toBanish.getVoiceState().inVoiceChannel()) {
            toBanish.getGuild().kickVoiceMember(toBanish).queue();
        }


        final EmbedBuilder confirmation = new EmbedBuilder();
        confirmation.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        confirmation.setTitle("Bestätigung");
        confirmation.setDescription("User " + toBanish.getAsMention() + " wurde durch " + originMsg.getAuthor().getAsMention()
                + "**" + " gemutet." + "**" + "\n Angegebener Grund: " + sanction.reason);
        originMsg.getChannel().sendMessage(confirmation.build()).queue();

        CommandHelper.logUserMute(sanction);

        final String performedSanction = "gemutet";
        final SanctionType sanctionType = SanctionType.MUTE;
        sendSanctionReason(toBanish.getUser(),sanctionType, performedSanction, sanction.reason, toBanish.getAsMention());
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
