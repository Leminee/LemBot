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
                embedBuilder.setDescription("Hallo, ich heiße **LemBot** und bin ein Bot für GoodQuestion:) \n" + "\n" +
                        "                -------------------- **BEFEHLSLISTE** -------------------- \n  " +
                        "\n" + " Prefix: **?**" + " \n" + " \n" +
                        "`?check <password>`: gibt zurück, ob Passwort unsicher ist" + " \n" +
                        "`?hmm`: Anzahl deiner geschickten Nachrichten auf GQ" + " \n" +
                        "`?hmb`: Anzahl deiner Bumps auf GQ" + " \n" +
                        "`?top`: Liste der Top 3 User mit den meisten Nachrichten" + "\n" +
                        "`?topb`: Liste der Top 3 Server-Bumper" + "\n" +
                        "`?topu`: Liste der 3 am häufigsten gepingten User" + "\n" +
                        "`?topc`: Liste der 3 aktivisten Channels" + "\n" +
                        "`?tope`: Liste der 3 am häufigsten benutzten Emojis" + "\n" +
                        "`?srole`: Liste aller Rollen auf GQ" + "\n" +
                        "`?aur`: Record der maximalen, aktiven User" + "\n" +
                        "`?hcb`: Zeigt, wie Quellcode eingefärbt wird" + " \n" +
                        "`?clear - <zahl>`: löscht Nachrichten" + " \n" +
                        "`?kick <User ID> [Grund]`: kickt User" + "\n" +
                        " (`?clear` und `?kick` *nur für Staff*)");

                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}
