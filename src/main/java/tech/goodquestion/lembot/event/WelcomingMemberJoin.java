package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.Helper;


public class WelcomingMemberJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull final GuildMemberJoinEvent event) {

        final String welcomeMessage = """
                Hallo [member], Willkommen auf **GoodQuestion (GQ)**!

                Du kannst Dir im Kanal [channel] Rollen zuweisen.

                """;

        final String newMemberAsMention = event.getMember().getAsMention();

        final String personalizedWelcomeMessage = welcomeMessage
                .replace("[member]", newMemberAsMention)
                .replace("[channel]", Config.getInstance().getChannel().getSelfRolesChannel().getAsMention());

        Config.getInstance().getChannel().getNewArrivalsChannel().sendMessage(personalizedWelcomeMessage).queue();

        Config.getInstance().getChannel().getJoinLeftChannel()
                .sendMessage(":arrow_right: User " + newMemberAsMention + " ist am " + Helper.getGermanDateTime() + " **gejoint**")
                .queue();

    }
}
