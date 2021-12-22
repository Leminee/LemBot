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

        try {
            final int messageAmountToDelete = Integer.parseInt(args[0]) + 1;

            final List<Message> messagesToDelete = message.getChannel().getHistory().retrievePast(messageAmountToDelete).complete();

            if (messageAmountToDelete > 30) {

                final EmbedBuilder errorAmountOfMessagesEmbed = new EmbedBuilder();

                Helper.createEmbed(errorAmountOfMessagesEmbed, "Fehler", ":x: Lösche bitte nicht mehr als 30 Nachrichten auf einmal!", EmbedColorHelper.ERROR);
                channel.sendMessage(errorAmountOfMessagesEmbed.build()).queue();
                return;
            }

            channel.deleteMessages(messagesToDelete).queue();

            final EmbedBuilder confirmationEmbed = new EmbedBuilder();

            Helper.createEmbed(confirmationEmbed, "Bestätigung", "Es wurden " + (messageAmountToDelete - 1) + " Nachrichten durch " + message.getAuthor().getAsMention() + " erfolgreich gelöscht!", EmbedColorHelper.SUCCESS);
            channel.sendMessage(confirmationEmbed.build()).queue();

        } catch (IllegalArgumentException illegalArgumentException) {
            if (illegalArgumentException.getMessage().equals("Message retrieval")) {
                final EmbedBuilder errorIAEEmbed = new EmbedBuilder();
                Helper.createEmbed(errorIAEEmbed, "Fehler", ":x: Mehr als 100 Nachrichten können nicht gelöscht werden!", EmbedColorHelper.ERROR);
                channel.sendMessage(errorIAEEmbed.build()).queue();


            } else if (illegalArgumentException instanceof NumberFormatException) {
                final EmbedBuilder errorNFEEmbed = new EmbedBuilder();
                Helper.createEmbed(errorNFEEmbed, "Fehler", ":x: Bitte gib bitte eine gültige Zahl an!", EmbedColorHelper.ERROR);
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
        return "`?clear <anzahl>`: Löscht die letzten <anzahl> Nachrichten";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
