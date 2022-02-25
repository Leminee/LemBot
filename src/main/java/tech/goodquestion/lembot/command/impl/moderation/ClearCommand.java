package tech.goodquestion.lembot.command.impl.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class ClearCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        try {
            final int amountMessagesToDelete = Integer.parseInt(args[0]) + 1;

            final List<Message> messagesToDelete = message.getChannel().getHistory().retrievePast(amountMessagesToDelete).complete();

            channel.deleteMessages(messagesToDelete).queue();

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Bestätigung");
            final String description = "Es wurden " + (amountMessagesToDelete - 1) + " Nachrichten durch " + message.getAuthor().getAsMention() + " erfolgreich gelöscht!";
            embedBuilder.setDescription(description);
            embedBuilder.setColor(Color.decode(EmbedColorHelper.SUCCESS));

            channel.sendMessageEmbeds(embedBuilder.build())
                    .queue( m -> m.delete().queueAfter(1, TimeUnit.HOURS));

        } catch (IllegalArgumentException illegalArgumentException) {

            if (illegalArgumentException.getMessage().equals("Message retrieval")) {
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                Helper.createEmbed(embedBuilder, "Fehler", ":x: Mehr als 100 Nachrichten können nicht gelöscht werden!", EmbedColorHelper.ERROR);
                channel.sendMessageEmbeds(embedBuilder.build()).queue();

            } else if (illegalArgumentException instanceof NumberFormatException) {
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                Helper.createEmbed(embedBuilder, "Fehler", ":x: Bitte gib bitte eine gültige Zahl an!", EmbedColorHelper.ERROR);
                channel.sendMessageEmbeds(embedBuilder.build()).queue();

            }
        }
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "`clear <anzahl>`: Löscht die letzten <anzahl> Nachrichten";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
