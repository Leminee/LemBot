package discord.bot.gq;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StoreMemberLeave extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        
        long userId = event.getUser().getIdLong();
        String userName = event.getUser().getName();
        String avatarUrl = event.getUser().getEffectiveAvatarUrl();
        String userTag = event.getUser().getAsTag();

        try {

            ConnectionToDB db = new ConnectionToDB();
            db.initialize();

            String insertDataQuery = "INSERT INTO user_leave (id_user_leave, id_discord, username,avatar_url,user_tag) VALUES (NULL,?,?,?,?);";

            PreparedStatement userDataInputPStatement = db.connection.prepareStatement(insertDataQuery);

            userDataInputPStatement.setLong(1, userId);
            userDataInputPStatement.setString(2, userName);
            userDataInputPStatement.setString(3, avatarUrl);
            userDataInputPStatement.setString(4, userTag);

            userDataInputPStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}