package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.config.Config;

public class InviteLinkDeletion extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String userMessage = event.getMessage().getContentRaw();

        if (!userMessage.contains("https://discord.gg") || !userMessage.contains("https://discord.com/invite/") || !userMessage.contains("https://discord.io/")) return;

        long authorId = event.getAuthor().getIdLong();
        long channelId = event.getChannel().getIdLong();
        long ownerId = Config.getInstance().getUsers().getOwnerId();
        Member author = event.getMember();
        assert author != null;
        boolean isStaff = author.hasPermission(Permission.MESSAGE_MANAGE);

        if (authorId == ownerId || isStaff || channelId == Config.getInstance().getChannels().getYourProjectsChannel().getIdLong()) {
            return;
        }

        event.getMessage().delete().queue();
        event.getChannel().sendMessage("Hier dürfen keine Invitelinks gepostet werden " + event.getAuthor().getAsMention() + "!").queue();
    }
}
