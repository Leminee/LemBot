package discord.bot.gq;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class CommandList extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String helpCommand = "help";

        if (userMessage.equalsIgnoreCase(BotMain.PREFIX + helpCommand) || userMessage.equalsIgnoreCase(BotMain.PREFIX + "hilfe")) {
            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("HILFE \n");
                embedBuilder.setColor(0x002d47);
                embedBuilder.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");
                embedBuilder.setDescription("Hallo, ich heiße **GQ** und bin ein Bot für GoodQuestion:) \n" + "\n" +
                        "                -------------------- **BEFEHLSLISTE** -------------------- \n  " +
                        "\n" + " Prefix:  `?`" + " \n" + " \n" +
                        "`?help`: Nachricht mit Befehlsliste \n  " +
                        "`?hcb`: Code Block Highlight" + " \n" +
                        "`?top`: Liste der Top 3 User mit den meisten Nachrichten" + "\n" +
                        "`?topb`: Liste der Top 3 Server-Bumper " + "\n" +
                        "`?topu`: Liste der 3 am häufigsten gepingten User" + "\n" +
                        "`?topc`: Liste der 3 aktivisten Channels" + "\n" +
                        "`?tope`: Liste der 3 am häufigsten benutzten Emojis auf GQ" + "\n" +
                        "`?srole`: Liste aller Rollen auf GQ" + "\n" +
                        "`?clear - <zahl>`: löscht Nachrichten" + " \n" +
                        "`?kick <@username> [Grund]`: kickt den User" + "\n" +
                        "`?ban <@username> [Grund]`: bannt den User" + "\n" +
                        "(`?clear`, `?kick`, `?ban` *nur für Staff*)");

                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}
