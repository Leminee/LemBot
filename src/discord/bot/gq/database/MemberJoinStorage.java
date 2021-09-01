package discord.bot.gq.database;

import discord.bot.gq.entities.MemberStatus;
import discord.bot.gq.lib.Helper;
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
