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

public class MessageDeletion extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String[] clearCommand = event.getMessage().getContentRaw().split("\\s+");
        Member authorCommand = event.getMessage().getMember();
        assert authorCommand != null;
        boolean authorHasMessageManagePermission = authorCommand.hasPermission(Permission.MESSAGE_MANAGE);


        if (clearCommand[0].equalsIgnoreCase(Helper.PREFIX + "clear") && clearCommand[1].equalsIgnoreCase("-")) {

            if (clearCommand.length < 3) {

                EmbedBuilder helpEmbed = new EmbedBuilder();
                Helper.createEmbed(helpEmbed, "Hilfe", "Richtige Command \" -> " + Helper.PREFIX + "clear - <Anzahl der zu löschenden Nachrichten als Zahl>", Color.lightGray);
                event.getChannel().sendMessage(helpEmbed.build()).queue();
                return;

            }

            if (!authorHasMessageManagePermission) {

                EmbedBuilder errorPermissionEmbed = new EmbedBuilder();

                Helper.createEmbed(errorPermissionEmbed, "Fehler", "Permission Denied", Color.RED);
                event.getChannel().sendMessage(errorPermissionEmbed.build()).queue();

            } else {

                try {

                    int amountMessages = Integer.parseInt(clearCommand[2]) + 1;

                    List<Message> messagesToDelete = event.getChannel().getHistory().retrievePast(amountMessages).complete();

                    if (amountMessages >= 31) {

                        EmbedBuilder errorAmountOfMessagesEmbed = new EmbedBuilder();

                        Helper.createEmbed(errorAmountOfMessagesEmbed, "Fehler", "Lösche bitte nicht mehr als 30 Nachrichten auf einmal!", Color.RED);
                        event.getChannel().sendMessage(errorAmountOfMessagesEmbed.build()).queue();

                    } else {

                        event.getChannel().deleteMessages(messagesToDelete).queue();

                        EmbedBuilder confirmationEmbed = new EmbedBuilder();

                        Helper.createEmbed(confirmationEmbed, "Bestätigung", "Es wurden " + clearCommand[2] + " Nachrichten durch " + authorCommand.getAsMention() + " erfolgreich gelöscht!", Color.GREEN);
                        event.getChannel().sendMessage(confirmationEmbed.build()).queue();
                    }

                } catch (IllegalArgumentException illegalArgumentException) {
                    if (illegalArgumentException.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {

                        EmbedBuilder errorIAEEmbed = new EmbedBuilder();

                        Helper.createEmbed(errorIAEEmbed, "Fehler", "Mehr als 100 Nachrichten können nicht gelöscht werden!", Color.RED);
                        event.getChannel().sendMessage(errorIAEEmbed.build()).queue();


                    }
                }
            }
        }
    }
}

