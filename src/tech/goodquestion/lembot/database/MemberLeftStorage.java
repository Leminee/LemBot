package tech.goodquestion.lembot.database;

import tech.goodquestion.lembot.entities.MemberStatus;
import tech.goodquestion.lembot.lib.Helper;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MemberLeftStorage extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        MemberStatus memberStatus = new MemberStatus();

        memberStatus.userId = event.getUser().getIdLong();
        memberStatus.userTag = event.getUser().getAsTag();
        memberStatus.userName = event.getUser().getName();
        memberStatus.avatarUrl = event.getUser().getEffectiveAvatarUrl();


        String userLeaveData = "INSERT INTO user_leave (id_user_leave,id_discord,user_tag,username,avatar_url) VALUES (NULL,?,?,?,?);";

        Helper.insertMemberStatus(userLeaveData, memberStatus);

        int currentNumberMember = event.getGuild().getMemberCount();
        Helper.insertCurrentNumberMember(currentNumberMember);


    }


}