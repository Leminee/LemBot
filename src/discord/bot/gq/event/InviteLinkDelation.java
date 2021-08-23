package discord.bot.gq.event;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InviteLinkDelation extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        long authorId = event.getAuthor().getIdLong();
        long ownerId = 739143338975952959L;
        Member author = event.getMember();


        if (authorId == ownerId) {
            return;
        }

        assert author != null;
        if (author.hasPermission(Permission.MESSAGE_MANAGE)) {
            return;
        }

        if (!userMessage.contains("https://discord.gg")) {
            return;
        }

        event.getMessage().delete().queue();
        event.getChannel().sendMessage("Hier dürfen keine Invitelinks gepostet werden " + event.getAuthor().getAsMention() + "!").queue();

    }
}
