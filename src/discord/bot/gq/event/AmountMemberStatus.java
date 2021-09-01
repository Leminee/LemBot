package discord.bot.gq.event;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class AmountMemberStatus extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {


        long memberStatusChannel = 811775664566698054L;
        VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(memberStatusChannel);
        assert voiceChannel != null;
        voiceChannel.getManager()
                .setName("Members: " + event.getGuild().getMemberCache().size())
                .queue();
    }


    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        long memberStatusChannel = 811775664566698054L;
        VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(memberStatusChannel);
        assert voiceChannel != null;
        voiceChannel.getManager()
                .setName("Members: " + event.getGuild().getMemberCache().size())
                .queue();
    }
}
