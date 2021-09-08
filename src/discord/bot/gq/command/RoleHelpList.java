package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class RoleHelpList extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String roleHelpCommand = "rhelp";

        if (Helper.isValidCommand(userMessageContent, roleHelpCommand)) {

            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

                EmbedBuilder roleHelpListEmbed = new EmbedBuilder();
                roleHelpListEmbed.setTitle("HILFE \n");
                roleHelpListEmbed.setColor(0xADD8E6);
                roleHelpListEmbed.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");
                roleHelpListEmbed.setDescription("""
                                        ------------ **ROLLEN-BEFEHLSLISTE** -------------\s

                        `?+bumper`: weist dem User <@&815922232106156033> zu 
                        `?-bumper`: entfernt dem User <@&815922232106156033>\s 
                        `?+dj`: weist dem User <@&869396259029540884> zu 
                        `?-dj`: entfernt dem User <@&869396259029540884>\s   
                        `?+ws`: weist dem User <@&882606255192047617> zu 
                        `?-ws`: entfernt dem User <@&882606255192047617>\s
                        """);

                event.getChannel().sendMessage(roleHelpListEmbed.build()).queue();
            }
        }
    }
}

