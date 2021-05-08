package discord.bot.gq;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class MemberJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        String welcomeChannelId = "779107500381175808";

        if (welcomeChannelId == null) {
            return;
        }

        String[] messages = {"""
Hallo [member], Willkommen auf **GoodQuestion (GQ)**!

Stelle Dich bitte hier kurz vor, damit ein Moderator Dir die entsprechenden Rollen zuweist! \n
Alternativ kannst Du Dir in dem folgenden Kanal Deine Rollen selbst zuweisen: <#779107472622223400>
"""};

        TextChannel channel = event.getGuild().getTextChannelById(welcomeChannelId);
        String avatarUrl = event.getUser().getEffectiveAvatarUrl();

        if (!event.getUser().isBot()) {

            String output = messages[0].replace("[member]", event.getMember().getAsMention());

            Objects.requireNonNull(channel).sendMessage(output + "\n" + avatarUrl).queue();

        }

    }
}