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


        ConnectionToDB connectionToDB = new ConnectionToDB();
        connectionToDB.initialize();

        String userJoinData = "INSERT INTO user_join (id_user_join,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";

        try (PreparedStatement preparedStatement = connectionToDB.getConnection().prepareStatement(userJoinData)) {


            preparedStatement.setLong(1, userId);
            preparedStatement.setBlob(2, Helper.changeCharacterEncoding(preparedStatement, userTag));
            preparedStatement.setBlob(3, Helper.changeCharacterEncoding(preparedStatement, userName));
            preparedStatement.setString(4, avatarUrl);

            preparedStatement.executeUpdate();

            int currentNumberMember = event.getGuild().getMemberCount();
            Helper.insertCurrentNumberMember(currentNumberMember);


        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

    }
}
