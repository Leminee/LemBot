package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

import java.util.List;

public class ClearCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        if (args.length < 1) {
            EmbedBuilder helpEmbed = new EmbedBuilder();
            Helper.createEmbed(helpEmbed, "Hilfe", "Richtige Benutzung: " + Helper.PREFIX + "clear <Anzahl der zu löschenden Nachrichten als Zahl>", EmbedColorHelper.NONE);
            channel.sendMessage(helpEmbed.build()).queue();
            return;
        }

        try {
            int messageAmountToDelete = Integer.parseInt(args[0]) + 1;

            List<Message> messagesToDelete = message.getChannel().getHistory().retrievePast(messageAmountToDelete).complete();

            if (messageAmountToDelete > 30) {

                EmbedBuilder errorAmountOfMessagesEmbed = new EmbedBuilder();

                Helper.createEmbed(errorAmountOfMessagesEmbed, "Fehler", "Lösche bitte nicht mehr als 30 Nachrichten auf einmal!", EmbedColorHelper.ERROR);
                channel.sendMessage(errorAmountOfMessagesEmbed.build()).queue();
                return;
            }

            channel.deleteMessages(messagesToDelete).queue();

            EmbedBuilder confirmationEmbed = new EmbedBuilder();

            Helper.createEmbed(confirmationEmbed, "Bestätigung", "Es wurden " + (messageAmountToDelete - 1) + " Nachrichten durch " + message.getAuthor().getAsMention() + " erfolgreich gelöscht!", EmbedColorHelper.CONFIRMATION);
            channel.sendMessage(confirmationEmbed.build()).queue();

        } catch (IllegalArgumentException iae) {
            if (iae.getMessage().equals("Message retrieval")) {
                EmbedBuilder errorIAEEmbed = new EmbedBuilder();
                Helper.createEmbed(errorIAEEmbed, "Fehler", "Mehr als 100 Nachrichten können nicht gelöscht werden!", EmbedColorHelper.ERROR);
                channel.sendMessage(errorIAEEmbed.build()).queue();


            } else if (iae instanceof NumberFormatException) {
                EmbedBuilder errorNFEEmbed = new EmbedBuilder();
                Helper.createEmbed(errorNFEEmbed, "Fehler", "Bitte gib bitte eine gültige Zahl an!", EmbedColorHelper.ERROR);
                channel.sendMessage(errorNFEEmbed.build()).queue();

            }
        }
    }

    @Override
    public boolean isPermitted(Member member) {
        return member.hasPermission(Permission.MESSAGE_MANAGE);
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "`?clear <anzahl>`: löscht die letzten <anzahl> Nachrichten aus dem Kanal";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
