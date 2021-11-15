package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.config.Config;


public class WelcomingMemberJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        if (event.getUser().isBot()) return;

        String welcomeMessage = """
Hallo [member], Willkommen auf **GoodQuestion (GQ)**!

Du kannst Dir im Kanal [channel] Rollen zuweisen!

""";
        String avatarUrl = event.getUser().getEffectiveAvatarUrl();
        String newMember = event.getMember().getAsMention();

        String output = welcomeMessage
                .replace("[member]", newMember)
                .replace("[channel]", Config.getInstance().getChannels().getSelfRolesChannel().getAsMention());

        Config.getInstance().getChannels().getNewArrivalsChannel().sendMessage(output).queue();
        Config.getInstance().getChannels().getNewArrivalsChannel().sendMessage(avatarUrl).queue();

    }
}
