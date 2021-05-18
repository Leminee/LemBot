package discord.bot.gq.event;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class WelcomingMemberJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        String welcomeChannelId = "779107500381175808";


        if (welcomeChannelId == null) {
            return;
        }

        String[] welcomeMessage = {"""
Hallo [member], Willkommen auf **GoodQuestion (GQ)**!

Stelle Dich bitte hier kurz vor, damit ein Moderator Dir die entsprechenden Rollen zuweist! \n
Alternativ kannst Du Dir in dem folgenden Kanal Deine Rollen selbst zuweisen: <#779107472622223400>
"""};
        TextChannel welcomeChannel = event.getGuild().getTextChannelById(welcomeChannelId);
        String avatarUrl = event.getUser().getEffectiveAvatarUrl();
        String newMember = event.getMember().getAsMention();

        if (!event.getUser().isBot()) {

            String output = welcomeMessage[0].replace("[member]", newMember);

            Objects.requireNonNull(welcomeChannel).sendMessage(output + "\n" + avatarUrl).queue();

        }

    }
}