package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.io.IOException;

public class MetaCommand implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Metafrage";
        final String url = "https://metafrage.de/";
        embedBuilder.setTitle(title, url);
        final String description = """
                 Eine Metafrage ist eine Frage über eine Frage, wie beispielsweise\s
                 „Darf ich etwas fragen?“ oder „Kennt sich jemand mit Computern aus?“.
                \s
                 Obwohl solche Fragen oft als Auftakt für Gespräche dienen,\s
                 sind sie hier nicht gern gesehen, da sie dazu neigen,\s
                 die Beantwortung der eigentlichen Frage zu verzögern oder sogar zu erschweren.
                \s
                 Mehr Informationen sind unter https://metafrage.de/ zu finden
                 \s""";
        embedBuilder.setDescription(description);
        final String color = EmbedColorHelper.META;
        embedBuilder.setColor(Color.decode(color));

        Helper.sendEmbed(embedBuilder, message, true);
    }

    @Override
    public String getName() {
        return "meta";
    }

    @Override
    public String getDescription() {
        return "`meta`: Verweis auf metafrage.de";
    }

    @Override
    public boolean isPermitted(Member member) {
        return true;
    }
}
