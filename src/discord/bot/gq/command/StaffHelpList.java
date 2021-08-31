package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class StaffHelpList extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String staffHelpCommand = "shelp";

        if (Helper.isValidCommand(userMessageContent, staffHelpCommand)) {

            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("HILFE \n");
                embed.setColor(0x8A2BE2);
                embed.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");
                embed.setDescription("""
                                            ------------ **STAFF-BEFEHLSLISTE** -------------\s

                            `?clear - <zahl>`: \n löscht Nachrichten\s\040 
                            `?warn <User> <Grund>`: \n weist dem User <@&879448018372395048> zu (Verwarnung)\s\040 
                            `?mute <User> <Grund>`: \n weist dem User <@&879329567947489352> zu (Mute)\s\040
                            `?unmute <User> <Grund>`: \n entfernt dem User <@&879329567947489352>
                            `?kick <User> <Grund>`: \n kickt den User\s\040\040
                            `?ban <User> <Grund>`: \n bannt den User""");

                event.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }
}
