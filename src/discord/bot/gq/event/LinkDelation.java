package discord.bot.gq.event;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LinkDelation extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        long channelId = event.getChannel().getIdLong();
        long newArrivalsChannelId = 779107500381175808L;
        Member author = event.getMember();
        assert author != null;
        boolean isStaff = author.hasPermission(Permission.MESSAGE_MANAGE);


        if (channelId != newArrivalsChannelId) {
            return;
        }

        if (isStaff) {
            return;
        }

        if (!userMessage.contains("https://")) {
            return;
        }

        event.getMessage().delete().queue();
        event.getChannel().sendMessage("Nachricht wurde gelöscht, da sie einen Link enthält, der nicht verifiziert werden konnte " + event.getAuthor().getAsMention() + "!").queue();

    }


}
