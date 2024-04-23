package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public class CommandsInfoCommand implements IBotCommand {


    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException, SQLException {

        final CommandManager commandManagerInstance = CommandManager.getInstance();

        final char commandPrefix = Config.getInstance().getBotConfig().getPrefix();

        final Set<String> commandList = commandManagerInstance.getHelpLists();

        commandList.removeIf(commandName -> !commandManagerInstance.getCommand(commandName).isPermitted(sender));

        final String[] commands = commandList.toArray(new String[0]);

        final int commandsPerPage = 20;
        final int minPage = 0;
        final int maxPage = commands.length / commandsPerPage;

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Command Info\n");
        embedBuilder.setColor(Color.decode(EmbedColorHelper.HELP));


        final String description = """
                
                ---------------    **COMMANDSINFO**    ---------------

                """;

        if (args.length != 0 && !isNumeric(args[0])) {

            if (commandList.contains(args[0])) {
                IBotCommand command = commandManagerInstance.getCommand(args[0]);

                embedBuilder.addField(command.getName(), command.getDescription(), false);
            }
        } else {
            int page;
            if (args.length == 0) {
                page = 0;
            } else {
                page = Integer.parseInt(args[0]) - 1;
            }

            if (!(page < minPage || page > maxPage)) {
                page = 0;
            }

            final int firstCommandNumber = page * commandsPerPage;
            for (int commandNumber = firstCommandNumber; commandNumber < (firstCommandNumber + commandsPerPage); commandNumber++) {
                IBotCommand command = commandManagerInstance.getCommand(commands[commandNumber]);

                embedBuilder.addField(command.getName(), command.getDescription(), false);
            }
            embedBuilder.setFooter("Seite " + (page + 1) + " von " + (maxPage + 1) + ". (" + commandPrefix + getName() + " [Seite])");
        }
        embedBuilder.setDescription(description);
        Helper.sendEmbed(embedBuilder, message, false);
    }

    @Override
    public String getName() {
        return "ci";
    }

    @Override
    public String getDescription() {
        return "`ci`: Informationen zu LemBot Commands";
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}