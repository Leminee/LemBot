package discord.bot.gq.database;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberJoinStorage extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        long userId = event.getUser().getIdLong();
        String userName = event.getUser().getName();
        String avatarUrl = event.getUser().getEffectiveAvatarUrl();
        String userTag = event.getUser().getAsTag();


        ConnectionToDB db = new ConnectionToDB();
        db.initialize();

        String userJoinData = "INSERT INTO user_join (id_user_join,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";

        try {

            PreparedStatement pS = db.getConnection().prepareStatement(userJoinData);

            pS.setLong(1, userId);
            pS.setBlob(2, Helper.changeCharacterEncoding(pS, userTag));
            pS.setBlob(3, Helper.changeCharacterEncoding(pS, userName));
            pS.setString(4, avatarUrl);

            pS.executeUpdate();

            int currentNumberMember = event.getGuild().getMemberCount();
            Helper.insertCurrentNumberMember(currentNumberMember);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
