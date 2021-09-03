package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Date;
import java.util.Objects;

public class AutoAnswering extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        String userName = Objects.requireNonNull(event.getMember()).getAsMention();
        Date currentDate = new Date();

        if (userMessageContent.equalsIgnoreCase(Helper.PREFIX + "hallo")) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Hi, Wie geht's dir? " + userName + " Was hast du heute schon gemacht? und was wirst du heute noch tun?").queue();
            }
        }

        if (userMessageContent.contains("Wie spät ist es?") || userMessageContent.contains("Wie spät?") ||
                userMessageContent.equalsIgnoreCase("Uhrzeit?") || userMessageContent.equalsIgnoreCase("Welche Uhrzeit?") ||
                userMessageContent.equalsIgnoreCase(Helper.PREFIX + "time")) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Uhrzeit: " + currentDate.toString().substring(11, 16)).queue();
            }
        }

        if (userMessageContent.equalsIgnoreCase(Helper.PREFIX + "ping")) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("pong").queue();

            }

        }

        if ((userMessageContent.startsWith("kennt sich wer") || (userMessageContent.contains("kennt sich wer") || userMessageContent.startsWith("kennt sich jemand") || userMessageContent.startsWith("Kennt sich jemand") ||
                userMessageContent.startsWith("kennt sich wer aus mit") || userMessageContent.contains("kann mir jemand helfen")) || userMessageContent.contains("Kennt sich wer mit") ||
                userMessageContent.contains("wer kennt sich aus") && userMessageContent.length() < 45)) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendMessage("Stelle einfach deine Frage - möglichst detailliert! " + userName).queue();

            }
        }

        if ((userMessageContent.startsWith(Helper.PREFIX + "hcb"))) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendMessage("https://cdn.discordapp.com/attachments/819694809765380146/832676790875062272/farbiger_code.png").queue();

            }
        }

        if ((userMessageContent.startsWith(Helper.PREFIX + "mq"))) {
            if (!event.getMember().getUser().isBot()) {
                event.getChannel().sendMessage("Stelle bitte keine Metagfrage, stelle einfach deine Frage - möglichst detailliert!").queue();

            }
        }

        if (userMessageContent.startsWith(Helper.PREFIX + "bsource")) {
            if (!event.getMember().getUser().isBot()) {
                EmbedBuilder botInfoEmbed = new EmbedBuilder()
                        .setTitle("LemBot Informationen")
                        .setColor(-9862987)
                        .setThumbnail("https://cdn.discordapp.com/avatars/815894805896888362/e8ac27a6bda7b0846bf5135d39e14943.webp?size=128")
                        .addField("Geschrieben in:", "Java (JDA)", false)
                        .addField("Geschrieben von:", "Lemin(e)#5985", false)
                        .addField("Source Code:", "https://github.com/Leminee/LemBot", false);
                event.getChannel().sendMessage(botInfoEmbed.build()).queue();
            }
        }
    }
}
