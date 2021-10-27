package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;

public class LinkDeletion extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String userMessage = event.getMessage().getContentRaw();

        if (!userMessage.contains("https://")) return;

        long channelId = event.getChannel().getIdLong();
        Member author = event.getMember();
        assert author != null;
        boolean isStaff = author.hasPermission(Permission.MESSAGE_MANAGE);

        if (channelId != Config.getInstance().getChannels().getNewArrivalsChannel().getIdLong() || isStaff) {
            return;
        }

        event.getMessage().delete().queue();
        event.getChannel().sendMessage("Nachricht wurde gelöscht, da sie einen Link enthält, der nicht verifiziert werden konnte " + event.getAuthor().getAsMention() + "!").queue();
    }

}
