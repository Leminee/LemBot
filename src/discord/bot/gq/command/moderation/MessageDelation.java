package discord.bot.gq.command.moderation;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class MessageDelation extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] clearCommand = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();


        if (clearCommand[0].equalsIgnoreCase(Helper.PREFIX + "clear") && clearCommand[1].equalsIgnoreCase("-")) {

            if (clearCommand.length < 3) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                Helper.createEmbed(embedBuilder, "Hilfe", "Richtige Command \" -> " +Helper.PREFIX + "clear - [Anzahl der zu löschenden Nachrichten als Zahl]", Color.lightGray, "https://www.lembot.de");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
                return;

            }

            assert authorCommand != null;
            if (!authorCommand.hasPermission(Permission.MESSAGE_MANAGE)) {

                EmbedBuilder embedBuilder = new EmbedBuilder();

                Helper.createEmbed(embedBuilder, "Fehler", "Permission Denied", Color.RED, "https://www.plane-dein-training.de");
                event.getChannel().sendMessage(embedBuilder.build()).queue();

            } else {

                try {

                    int amountMessages = Integer.parseInt(clearCommand[2]) + 1;

                    List<Message> messagesToDelete = event.getChannel().getHistory().retrievePast(amountMessages).complete();

                    if (amountMessages >= 31) {

                        EmbedBuilder embedBuilder = new EmbedBuilder();

                        Helper.createEmbed(embedBuilder, "Fehler", "Lösche bitte nicht mehr als 30 Nachrichten auf einmal!", Color.RED, "https://www.plane-dein-training.de");
                        event.getChannel().sendMessage(embedBuilder.build()).queue();

                    } else {

                        event.getChannel().deleteMessages(messagesToDelete).queue();

                        EmbedBuilder embedBuilder = new EmbedBuilder();

                        Helper.createEmbed(embedBuilder, "Bestätigung", "Es wurden " +clearCommand[2] + " Nachrichten durch " + authorCommand.getAsMention() + " erfolgreich gelöscht!", Color.GREEN, "https://www.plane-dein-training.de");
                        event.getChannel().sendMessage(embedBuilder.build()).queue();
                    }

                } catch (IllegalArgumentException illegalArgumentException) {
                    if (illegalArgumentException.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {

                        EmbedBuilder embedBuilder = new EmbedBuilder();

                        Helper.createEmbed(embedBuilder, "Fehler", "Mehr als 100 Nachrichten können nicht gelöscht werden!", Color.RED, "https://www.plane-dein-training.de");
                        event.getChannel().sendMessage(embedBuilder.build()).queue();


                    }
                }
            }
        }

    }
}

