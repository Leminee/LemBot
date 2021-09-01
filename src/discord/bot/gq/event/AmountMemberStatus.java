package discord.bot.gq.event;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class AmountMemberStatus extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {


        long memberStatusChannelId = 811775664566698054L;
        VoiceChannel memberStatus = event.getGuild().getVoiceChannelById(memberStatusChannelId);
        assert memberStatus != null;
        memberStatus.getManager()
                .setName("Members: " + event.getGuild().getMemberCount())
                .queue();
    }


    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        long memberStatusChannelId = 811775664566698054L;
        VoiceChannel memberStatus = event.getGuild().getVoiceChannelById(memberStatusChannelId);
        assert memberStatus != null;
        memberStatus.getManager()
                .setName("Members: " + event.getGuild().getMemberCount())
                .queue();
    }
}
