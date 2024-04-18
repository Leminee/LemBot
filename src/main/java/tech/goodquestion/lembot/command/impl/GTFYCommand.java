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
import java.sql.SQLException;

public class GTFYCommand implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException, SQLException {

        if (args.length < 1) {

            Helper.sendError(message, ":x: Gebe eine Frage an!");
            return;
        }
        final StringBuilder stringBuilder = new StringBuilder();

        for (String arg : args) {
            stringBuilder.append(arg).append("%20");
        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Antwort";
        final String baseUrl = "https://googlethatforyou.com/?q=";
        embedBuilder.setTitle(title);

        final String description = baseUrl + stringBuilder;

        embedBuilder.setDescription(description);
        final String color = EmbedColorHelper.GTFY;
        embedBuilder.setColor(Color.decode(color));

        Helper.sendEmbed(embedBuilder, message, false);

    }

    @Override
    public String getName() {
        return "gtfy";
    }

    @Override
    public String getDescription() {
        return "`gtfy <question>`: googelt die Frage";
    }

    @Override
    public boolean isPermitted(Member member) {
        return true;
    }
}