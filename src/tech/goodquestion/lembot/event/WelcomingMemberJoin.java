package tech.goodquestion.lembot.event;

import tech.goodquestion.lembot.config.db.ConfigSelection;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class WelcomingMemberJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        ConfigSelection configSelection = new ConfigSelection();
        configSelection.selectNewArrivalsChannelId();

        String[] welcomeMessage = {"""
Hallo [member], Willkommen auf **GoodQuestion (GQ)**!

Du kannst Dich hier kurz vorstellen und Dir im Kanal <#779107472622223400> Rollen zuweisen!\040

"""};
        TextChannel welcomeChannel = event.getGuild().getTextChannelById(configSelection.getNewArrivalsChannelId());
        String avatarUrl = event.getUser().getEffectiveAvatarUrl();
        String newMember = event.getMember().getAsMention();

        if (!event.getUser().isBot()) {

            String output = welcomeMessage[0].replace("[member]", newMember);

            Objects.requireNonNull(welcomeChannel).sendMessage(output + "\n" + avatarUrl).queue();

        }

    }
}