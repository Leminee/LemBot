package discord.bot.gq.event;

import discord.bot.gq.database.ConnectionToDB;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
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

        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        try {

            PreparedStatement pS = db.getConnection().prepareStatement(userVerificationCheck);

            pS.setLong(1, joinedUserId);

            ResultSet rS = pS.executeQuery();

            long userId = event.getMember().getIdLong();

            Role mutedRole = event.getGuild().getRoleById(879329567947489352L);

            if (rS.next()) {

                int number = rS.getInt(1);

                if (number == 1) {

                    assert mutedRole != null;
                    event.getGuild().addRoleToMember(userId, mutedRole).queue();

                }
            }


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {


        ConnectionToDB db = new ConnectionToDB();
        db.initialize();


        long removedRoleId = event.getRoles().get(0).getIdLong();
        long userId = event.getMember().getIdLong();
        String enableUser = "0";


        if (removedRoleId != 879329567947489352L) {
            return;
        }


        String userVerificationCheck = "SELECT activ FROM muted_user WHERE id_discord = ?;";

        try {

            PreparedStatement pS = db.getConnection().prepareStatement(userVerificationCheck);

            pS.setLong(1, userId);

            ResultSet rS = pS.executeQuery();

            if (rS.next()) {


                String userUnmute = "UPDATE muted_user SET activ = ? WHERE id_discord = ? ORDER BY muted_on DESC LIMIT 1;";

                PreparedStatement pSTwo = db.getConnection().prepareStatement(userUnmute);

                pSTwo.setString(1, enableUser);
                pSTwo.setLong(2, userId);

                pSTwo.executeUpdate();

            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }
}
