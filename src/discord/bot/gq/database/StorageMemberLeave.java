package discord.bot.gq.database;

import discord.bot.gq.Helper;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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

            String insertUserLeaveData = "INSERT INTO user_leave (id_user_leave, id_discord, username,avatar_url,user_tag) VALUES (NULL,?,?,?,?);";

            PreparedStatement pS = db.getConnection().prepareStatement(insertUserLeaveData);

            pS.setLong(1, userId);
            pS.setBlob(2, Helper.setTransformedString(pS,userName));
            pS.setString(3, avatarUrl);
            pS.setString(4, userTag);
            pS.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }



}