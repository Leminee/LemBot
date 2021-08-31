package discord.bot.gq.event;

import discord.bot.gq.database.ConnectionToDB;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthorization extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        long joinedUserId = event.getMember().getIdLong();

        String userVerificationCheck = "SELECT activ FROM muted_user WHERE id_discord = ? ORDER BY muted_on DESC LIMIT 1;";

        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(userVerificationCheck)) {


            preparedStatement.setLong(1, joinedUserId);

            ResultSet resultSet = preparedStatement.executeQuery();

            long userId = event.getMember().getIdLong();

            Role mutedRole = event.getGuild().getRoleById(879329567947489352L);

            if (resultSet.next()) {

                int number = resultSet.getInt(1);

                if (number == 1) {

                    assert mutedRole != null;
                    event.getGuild().addRoleToMember(userId, mutedRole).queue();

                }
            }


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }
}
