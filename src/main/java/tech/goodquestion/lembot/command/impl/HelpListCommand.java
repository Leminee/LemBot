package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.library.EmbedColorHelper;

import java.awt.*;
import java.util.List;
import java.util.*;

import static tech.goodquestion.lembot.BotMain.PREFIX;

public class HelpListCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {


        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("HILFE \n");
        embedBuilder.setColor(Color.decode(EmbedColorHelper.HELP));
        embedBuilder.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");

        String queriedHelpList = "default";

        if (args.length > 0) {
            queriedHelpList = args[0].toLowerCase(Locale.ROOT);
        }

        if (queriedHelpList.equals("-")) {
            EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.decode(EmbedColorHelper.HELP))
                    .setTitle("Verfügbare Help-Listen")
                    .setDescription(PREFIX+"help " + String.join("\n" +PREFIX+"help ",CommandManager.getInstance().getHelpLists()));
            message.getChannel().sendMessageEmbeds(embedBuilder1.build()).queue();
            return;
        }

        StringBuilder descriptionBuilder = new StringBuilder("Hallo, ich heiße **LemBot** und bin ein Bot für **GoodQuestion** :)");
        descriptionBuilder.append("\n----------------- **BEFEHLSLISTE** -----------------\n");
        descriptionBuilder.append("\n");

        List<IBotCommand> commandsOnHelpList = new ArrayList<>();

        for (IBotCommand command : CommandManager.getInstance().getCommands().values()) {
            if (!Objects.equals(command.getHelpList(), queriedHelpList)) continue;
            commandsOnHelpList.add(command);
        }

        commandsOnHelpList.sort(Comparator.comparing(IBotCommand::getName));
        commandsOnHelpList.forEach(c -> descriptionBuilder.append(c.getDescription()).append("\n"));

        embedBuilder.setDescription(descriptionBuilder.toString());
        message.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "`?help -`: Verfügbare Command-Listen";
    }

    @Override
    public String getHelpList() {
        return "default";
    }

}