package tech.goodquestion.lembot.database;

import tech.goodquestion.lembot.entities.MemberStatus;
import tech.goodquestion.lembot.lib.Helper;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberJoinStorage extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        MemberStatus memberStatus = new MemberStatus();

        memberStatus.userId = event.getUser().getIdLong();
        memberStatus.userTag = event.getUser().getAsTag();
        memberStatus.userName = event.getUser().getName();
        memberStatus.avatarUrl = event.getUser().getEffectiveAvatarUrl();


        String userJoinData = "INSERT INTO user_join (id_user_join,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";

        Helper.insertMemberStatus(userJoinData, memberStatus);

        int currentNumberMember = event.getGuild().getMemberCount();
        Helper.insertCurrentNumberMember(currentNumberMember);

    }
}
