package tech.goodquestion.lembot.command;

import tech.goodquestion.lembot.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Objects;

public class BumpRole extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        long authorCommandId = Objects.requireNonNull(event.getMember()).getIdLong();
        String authorCommandAsMention = event.getMember().getAsMention();
        String addBumperRoleCommand = "+bumper";
        String removeBumperRoleCommand = "-bumper";
        Role bumpRoleId = event.getGuild().getRoleById(815922232106156033L);


        if (Helper.isValidCommand(userMessageContent, addBumperRoleCommand)) {

            assert bumpRoleId != null;
            event.getGuild().addRoleToMember(authorCommandId, bumpRoleId).queue();

            EmbedBuilder roleAddedEmbed = new EmbedBuilder();

            String embedDescription = "<@&815922232106156033> wurde Dir erfolgreich zugewiesen <:bumper:821144354445328384> " + authorCommandAsMention;

            Helper.createEmbed(roleAddedEmbed, "Bestätigung", embedDescription, Color.GREEN);

            event.getChannel().sendMessage(roleAddedEmbed.build()).queue();
            return;


        }

        if (Helper.isValidCommand(userMessageContent, removeBumperRoleCommand)) {

            assert bumpRoleId != null;
            event.getGuild().removeRoleFromMember(authorCommandId, bumpRoleId).queue();

            EmbedBuilder roleRemovedEmbed = new EmbedBuilder();

            String embedDescription = "<@&815922232106156033> wurde erfolgreich entfernt :frowning: " + authorCommandAsMention;

            Helper.createEmbed(roleRemovedEmbed, "Bestätigung", embedDescription, Color.GREEN);
            event.getChannel().sendMessage(roleRemovedEmbed.build()).queue();

        }

    }

}
