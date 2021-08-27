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

Du kannst Dich hier kurz vorstellen und Dir im Kanal <#779107472622223400> Rollen zuweisen! 

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