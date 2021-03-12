package discord.bot.gq;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StorageMemberJoin extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        long userId = event.getUser().getIdLong();
        String userName = event.getUser().getName();
        String avatarUrl = event.getUser().getEffectiveAvatarUrl();


        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        String insertDataQuery = "INSERT INTO user_join (id_user_join, id_discord, username,avatar_url) VALUES (NULL,?,?,?);";

        try {

            PreparedStatement userDataInputPStatement = db.connection.prepareStatement(insertDataQuery);

            userDataInputPStatement.setLong(1, userId);
            userDataInputPStatement.setString(2, userName);
            userDataInputPStatement.setString(3, avatarUrl);

            userDataInputPStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
