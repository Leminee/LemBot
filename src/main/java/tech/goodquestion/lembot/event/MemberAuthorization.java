package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.DatabaseConnector;
import tech.goodquestion.lembot.entity.OccurredException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberAuthorization extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull final GuildMemberJoinEvent event) {

        final long joinedMemberId = event.getMember().getIdLong();

        final String userVerificationCheck = "SELECT activ FROM muted_user WHERE id_discord = ? ORDER BY muted_at DESC LIMIT 1;";

        Connection connection = DatabaseConnector.openConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(userVerificationCheck)) {

            preparedStatement.setLong(1, joinedMemberId);

            ResultSet resultSet = preparedStatement.executeQuery();

            final long userId = event.getMember().getIdLong();

            if (resultSet.next()) {

                final boolean isMuted = resultSet.getInt(1) == 1;

                if (isMuted) {

                    event.getGuild().addRoleToMember(userId, Config.getInstance().getRoleConfig().getMuteRole()).queue();
                }
            }

        } catch (SQLException sqlException) {

            System.out.println(sqlException.getMessage());

            CommandHelper.logException(OccurredException.getOccurredExceptionData(sqlException, this.getClass().getName()));
        }
    }
}
