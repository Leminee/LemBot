package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class HelpList extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String helpCommand = "help";

        if (Helper.isValidCommand(userMessage, helpCommand) || Helper.isValidCommand(userMessage, "hilfe")) {
            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("HILFE \n");
                embedBuilder.setColor(0x002d47);
                embedBuilder.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");
                embedBuilder.setDescription("""
                        Hallo, ich heiße **LemBot** und bin ein Bot für GoodQuestion:)\s

                                        -------------------- **BEFEHLSLISTE** --------------------\s
                         \s
                         Prefix: **?**\s
                        \s
                        `?check <password>`: gibt zurück, ob Passwort unsicher ist\s
                        `?hmm`: Anzahl deiner geschickten Nachrichten auf GQ\s
                        `?hmb`: Anzahl deiner Bumps auf GQ\s
                        `?topu`: Liste der Top 3 User mit den meisten Nachrichten
                        `?topb`: Liste der Top 3 Server-Bumper
                        `?topp`: Liste der 3 am häufigsten gepingten User
                        `?topc`: Liste der 3 aktivisten Channels
                        `?tope`: Liste der 3 am häufigsten benutzten Emojis
                        `?srole`: Liste aller Rollen auf GQ
                        `?aur`: Record der maximalen, aktiven User
                        `?hcb`: Zeigt, wie Quellcode eingefärbt wird\s
                        `?clear - <zahl>`: löscht Nachrichten\s 
                        `?mute <User ID> [Grund]`: weist dem User @Muted zu\s 
                        `?kick <User ID> [Grund]`: kickt User\s  
                        `?ban <User ID> [Grund]`: bant User
                         (`?clear`, `?mute`, `?kick` und `?ban` *nur für Staff*)""");

                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}
