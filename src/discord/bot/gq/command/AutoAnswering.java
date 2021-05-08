package discord.bot.gq.command;

import discord.bot.gq.BotMain;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Date;
import java.util.Objects;

public class AutoAnswering extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String userName = Objects.requireNonNull(event.getMember()).getAsMention();
        Date date = new Date();

        if (userMessage.equalsIgnoreCase(BotMain.PREFIX + "hallo")) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Hi, Wie geht's dir? " + userName + " Was hast du heute schon gemacht? und was wirst du heute noch tun?").queue();
            }
        }

        if (userMessage.equalsIgnoreCase("Wie spät ist es?") || userMessage.equalsIgnoreCase("Wie spät?") ||
                userMessage.equalsIgnoreCase("Uhrzeit?") || userMessage.equalsIgnoreCase("Welche Uhrzeit?") ||
                userMessage.equalsIgnoreCase(BotMain.PREFIX + "time")) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Uhrzeit: " + date.toString().substring(11, 16)).queue();
            }
        }

        if (userMessage.equalsIgnoreCase(BotMain.PREFIX + "ping")) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("pong").queue();

            }

        }

        if ((userMessage.startsWith("kennt sich wer") || (userMessage.contains("kennt sich wer") || userMessage.startsWith("kennt sich jemand") || userMessage.startsWith("Kennt sich jemand") ||
                userMessage.startsWith("kennt sich wer aus mit") || userMessage.contains("kann mir jemand helfen")) || userMessage.contains("Kennt sich wer mit") ||
                userMessage.contains("wer kennt sich aus") && userMessage.length() < 45)) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendMessage("Stelle einfach deine Frage - möglichst detailliert! " + userName).queue();

            }
        }

        if ((userMessage.equalsIgnoreCase(BotMain.PREFIX + "hcb"))){
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendMessage("https://cdn.discordapp.com/attachments/819694809765380146/832676790875062272/farbiger_code.png").queue();

            }
        }
    }
}
