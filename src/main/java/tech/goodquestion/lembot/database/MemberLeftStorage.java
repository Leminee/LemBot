package tech.goodquestion.lembot.database;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.Helper;

import java.util.Objects;

public class MemberLeftStorage extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        CommandHelper.logUserLeave(event.getUser());
        CommandHelper.logMemberAmount(event.getGuild());


        Objects.requireNonNull(event.getGuild().getTextChannelById(Config.getInstance().getChannel().getLogChannel().getIdLong()))
                .sendMessage(":door: User **" + event.getUser().getAsTag() + "** hat den Server am " + Helper.getGermanDateTime() +" **verlassen**")
                .queue();
    }
}