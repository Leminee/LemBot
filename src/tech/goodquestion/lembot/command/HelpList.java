package tech.goodquestion.lembot.command;

import tech.goodquestion.lembot.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class HelpList extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String helpCommand = "help";

        if (Helper.isValidCommand(userMessageContent, helpCommand) || Helper.isValidCommand(userMessageContent, "hilfe")) {
            if (!Objects.requireNonNull(event.getMember()).getUser().isBot()) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("HILFE \n");
                embedBuilder.setColor(0x002d47);
                embedBuilder.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");
                embedBuilder.setDescription("""
                        Hallo, ich heiße **LemBot** und bin ein Bot für GoodQuestion:)\s

                                        ----------------- **BEFEHLSLISTE** -----------------\s
                         \s
                         Prefix: **?**\s
                        \s
                        `?check <password>`: überprüft, ob Passwort unsicher ist\s
                        `?bsource`: Informationen zum Code von Lembot
                        `?hmm`: Anzahl deiner geschickten Nachrichten auf GQ\s
                        `?hmb`: Anzahl deiner Bumps auf GQ\s
                        `?topu`: Liste der Top 3 User mit den meisten Nachrichten
                        `?topb`: Liste der Top 3 Server-Bumper\040
                        `?tope`: Liste der 3 am häufigsten benutzten Emojis\040\040
                        `?topp`: Liste der 3 am häufigsten gepingten User
                        `?topc`: Liste der 3 aktivisten Channels
                        `?aur`: Record an gleichzeitig aktiven Usern
                        `?srole`: Liste aller Rollen auf GQ\040
                        `?rhelp`: Zuweisen anderer Rollen
                        `?shelp`: Staff-Befehlsliste
                        """);

                event.getChannel().sendMessage(embedBuilder.build()).queue();

            }

        }

    }

}
