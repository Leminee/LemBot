package tech.goodquestion.lembot.command.impl;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.library.*;
import tech.goodquestion.lembot.library.parser.LocalDateTimeFormatter;
import tech.goodquestion.lembot.library.parser.LocalDateTimeParser;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;

public final class ParserCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {

        if (args.length < 1) return;

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Parser";

        try {

            final LocalDateTime parsedInput = LocalDateTimeParser.parse(message.getContentRaw());

            assert parsedInput != null;
            final String description = "```js\n" + LocalDateTimeFormatter.toGermanFormat(parsedInput).replace("um", "") + "```\n";

            Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.SUCCESS);

            Helper.sendEmbed(embedBuilder, message, true);

        } catch (NumberFormatException | DateTimeException numberFormatException) {

            Helper.createEmbed(embedBuilder, title, "```" + numberFormatException.getMessage() + "```", EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
        }
    }

    @Override
    public String getName() {
        return "p";
    }

    @Override
    public String getDescription() {
        return "`p <input>`: Parst den Input in DateTime";
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }
}