package tech.goodquestion.lembot.command;

import tech.goodquestion.lembot.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Objects;

public class DjRole extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        long authorCommandId = Objects.requireNonNull(event.getMember()).getIdLong();
        String authorCommandAsMention = event.getMember().getAsMention();
        String addDjRoleCommand = "+dj";
        String removeDjRoleCommand = "-dj";
        Role djRoleId = event.getGuild().getRoleById(869396259029540884L);


        if (Helper.isValidCommand(userMessageContent, addDjRoleCommand)) {

            assert djRoleId != null;
            event.getGuild().addRoleToMember(authorCommandId, djRoleId).queue();

            EmbedBuilder roleAddedEmbed = new EmbedBuilder();

            String embedDescription = "<@&869396259029540884> wurde Dir erfolgreich zugewiesen " + authorCommandAsMention;

            Helper.createEmbed(roleAddedEmbed, "Bestätigung", embedDescription, Color.GREEN);

            event.getChannel().sendMessage(roleAddedEmbed.build()).queue();
            return;


        }

        if (Helper.isValidCommand(userMessageContent, removeDjRoleCommand)) {

            assert djRoleId != null;
            event.getGuild().removeRoleFromMember(authorCommandId, djRoleId).queue();

            EmbedBuilder roleRemovedEmbed = new EmbedBuilder();

            String embedDescription = "<@&869396259029540884> wurde erfolgreich entfernt " + authorCommandAsMention;

            Helper.createEmbed(roleRemovedEmbed, "Bestätigung", embedDescription, Color.GREEN);
            event.getChannel().sendMessage(roleRemovedEmbed.build()).queue();

        }

    }

}
