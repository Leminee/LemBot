package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.entity.Sanction;
import tech.goodquestion.lembot.entity.SanctionType;
import tech.goodquestion.lembot.library.EmbedColorHelper;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MuteCommand extends UserBanishCommand {

    @Override
    public void banishUser(Member toBanish, Sanction sanction, Message originMsg) {

        final List<Role> userRoles = Objects.requireNonNull(toBanish).getRoles();

        for (final Role role : userRoles) {

            toBanish.getGuild().removeRoleFromMember(sanction.userId, role).queue();
        }

        toBanish.getGuild().addRoleToMember(sanction.userId, Config.getInstance().getRole().getMuteRole()).queue();

        if (Objects.requireNonNull(toBanish.getVoiceState()).inAudioChannel()) {
            toBanish.getGuild().kickVoiceMember(toBanish).queue();
        }


        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));
        embedBuilder.setTitle("Bestätigung");
        embedBuilder.setDescription("User " + toBanish.getAsMention() + " wurde durch " + originMsg.getAuthor().getAsMention()
                + "**" + " gemutet." + "**" + "\n Angegebener Grund: " + sanction.reason);
        originMsg.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

        CommandHelper.logUserMute(sanction);

        final String performedSanction = "gemutet";
        final SanctionType sanctionType = SanctionType.MUTE;

        sendSanctionReason(toBanish.getUser(),sanctionType, performedSanction, sanction.reason);
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


    private void unmuteUser(int daysDelay, Member memberToUnmute) {

        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);


        final Runnable runnable = () -> {

            memberToUnmute.getGuild().removeRoleFromMember(memberToUnmute.getIdLong(), Config.getInstance().getRole().getMuteRole()).queue();
            memberToUnmute.getGuild().addRoleToMember(memberToUnmute.getIdLong(), Config.getInstance().getRole().getCodingRole()).queue();

            Connection connection = DatabaseConnector.openConnection();

            String userMuted = "SELECT activ FROM muted_user WHERE id_discord = ?;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(userMuted)) {

                preparedStatement.setLong(1, memberToUnmute.getIdLong());

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {


                    String userUnmute = "UPDATE muted_user SET activ = ? WHERE id_discord = ? ORDER BY muted_at DESC LIMIT 1;";

                    PreparedStatement preparedStatementOne = connection.prepareStatement(userUnmute);

                    preparedStatementOne.setString(1, "0");
                    preparedStatementOne.setLong(2, memberToUnmute.getIdLong());

                    preparedStatementOne.executeUpdate();
                }

            } catch (SQLException sqlException) {
                System.out.println(sqlException.getMessage());

                CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
            }
        };

        scheduledExecutorService.schedule(runnable, daysDelay,TimeUnit.DAYS);
    }
}
