package discord.bot.gq.db;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StorageMemberLeave extends ListenerAdapter {

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

            PreparedStatement userDataInput = db.connection.prepareStatement(insertDataQuery);

            userDataInput.setLong(1, userId);
            byte[] byteA = userName.getBytes();
            Blob blob = userDataInput.getConnection().createBlob();
            blob.setBytes(1, byteA);
            userDataInput.setBlob(2, blob);
            userDataInput.setString(3, avatarUrl);
            userDataInput.setString(4, userTag);

            userDataInput.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}