package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Objects;

public class WorkshopRole extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        long authorCommandId = Objects.requireNonNull(event.getMember()).getIdLong();
        String authorCommandAsMention = event.getMember().getAsMention();
        String addWorkshopRoleCommand = "+ws";
        String removeWorkshopRoleCommand = "-ws";
        Role wsRoleId = event.getGuild().getRoleById(882606255192047617L);


        if (Helper.isValidCommand(userMessageContent, addWorkshopRoleCommand)) {

            assert wsRoleId != null;
            event.getGuild().addRoleToMember(authorCommandId, wsRoleId).queue();

            EmbedBuilder roleAddedEmbed = new EmbedBuilder();

            String embedDescription = "<@&882606255192047617> wurde Dir erfolgreich zugewiesen " + authorCommandAsMention;

            Helper.createEmbed(roleAddedEmbed, "Bestätigung", embedDescription, Color.GREEN);

            event.getChannel().sendMessage(roleAddedEmbed.build()).queue();
            return;


        }

        if (Helper.isValidCommand(userMessageContent, removeWorkshopRoleCommand)) {

            assert wsRoleId != null;
            event.getGuild().removeRoleFromMember(authorCommandId, wsRoleId).queue();

            EmbedBuilder roleRemovedEmbed = new EmbedBuilder();

            String embedDescription = "<@&882606255192047617> wurde erfolgreich entfernt " + authorCommandAsMention;

            Helper.createEmbed(roleRemovedEmbed, "Bestätigung", embedDescription, Color.GREEN);
            event.getChannel().sendMessage(roleRemovedEmbed.build()).queue();

        }

    }

}



