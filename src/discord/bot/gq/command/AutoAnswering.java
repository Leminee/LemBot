package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class AutoAnswering extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        String userName = Objects.requireNonNull(event.getMember()).getAsMention();
        Date currentDate = new Date();

        if (userMessage.equalsIgnoreCase(Helper.PREFIX + "hallo")) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Hi, Wie geht's dir? " + userName + " Was hast du heute schon gemacht? und was wirst du heute noch tun?").queue();
            }
        }

        if (userMessage.contains("Wie spät ist es?") || userMessage.contains("Wie spät?") ||
                userMessage.equalsIgnoreCase("Uhrzeit?") || userMessage.equalsIgnoreCase("Welche Uhrzeit?") ||
                userMessage.equalsIgnoreCase(Helper.PREFIX + "time")) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Uhrzeit: " + currentDate.toString().substring(11, 16)).queue();
            }
        }

        if (userMessage.equalsIgnoreCase(Helper.PREFIX + "ping")) {
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

        if ((userMessage.startsWith(Helper.PREFIX + "hcb"))) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendMessage("https://cdn.discordapp.com/attachments/819694809765380146/832676790875062272/farbiger_code.png").queue();

            }
        }

        if ((userMessage.startsWith(Helper.PREFIX + "mq"))) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendMessage("Stelle bitte keine Metagfrage, stelle einfach deine Frage - möglichst detailliert!").queue();

            }
        }

        if (userMessage.startsWith(Helper.PREFIX + "source")) {
            if (!event.getMember().getUser().isBot()) {
                EmbedBuilder botInfoEmbed = new EmbedBuilder()
                        .setTitle("LemBot Informationen")
                        .setColor(-9862987)
                        .setThumbnail("https://cdn.discordapp.com/avatars/815894805896888362/e8ac27a6bda7b0846bf5135d39e14943.webp?size=128")
                        .addField("Sprache", "Java", false)
                        .addField("Source Code", "https://github.com/Leminee/LemBot", false);
                event.getChannel().sendMessage(botInfoEmbed.build()).queue();
            }
        }
    }
}
