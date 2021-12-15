package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;

public class AmountMemberStatus extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        Config.getInstance().getChannel().getMemberCountChannel().getManager()
                .setName("Members: " + event.getGuild().getMemberCount())
                .queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

        Config.getInstance().getChannel().getMemberCountChannel().getManager()
                .setName("Members: " + event.getGuild().getMemberCount())
                .queue();
    }

}
