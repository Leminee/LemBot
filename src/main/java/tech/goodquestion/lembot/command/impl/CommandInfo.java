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

public class CommandInfo implements IBotCommand {

    public final static CommandManager commandManagerInstance = CommandManager.getInstance();

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException, SQLException {
        final char commandPrefix = Config.getInstance().getBotConfig().getPrefix();


        Set<String> commandList = commandManagerInstance.getHelpLists();
        // Die Commands, für die der User keine Rechte hat sollen nicht angezeigt werden
        commandList.removeIf(commandName -> !commandManagerInstance.getCommand(commandName).isPermitted(sender));

        String[] commandListArray = commandList.toArray(new String[0]);

        int commandsPerPage = 20;
        int minPage = 0;
        int maxPage = commandListArray.length / commandsPerPage;

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Command Info\n");
        embedBuilder.setColor(Color.decode(EmbedColorHelper.HELP));


        Config configInstance = Config.getInstance();
        String description = "Hallo, ich heiße **" + configInstance.getBotConfig().getName() + "** und bin ein Bot für **" + configInstance.getServerName() + "** :)" +
                "\n"+
                "\n" +
                "---------------    **BEFEHLSINFO**    ---------------" +
                "\n"+
                "\n";

        if(args.length != 0 && !isNumeric(args[0])) {
            // Die Information für einen bestimmten Command wird angefragt

            if(commandList.contains(args[0])) {
                // Command existiert
                IBotCommand command = commandManagerInstance.getCommand(args[0]);

                embedBuilder.addField(command.getName(), command.getDescription(), false);
            }
        } else {
            // Kein bestimmter Command angegeben, es werden alle der angegebenen Seite (args[0]) angezeigt
            int page;
            if(args.length == 0) {
                page = 0;
            } else {
                page = Integer.parseInt(args[0]) - 1;
            }

            if(!(page < minPage || page > maxPage)) {
                page = 0;
            }

            int firstCommandNumber = page * commandsPerPage;
            for(int commandNumber = firstCommandNumber; commandNumber < (firstCommandNumber + commandsPerPage); commandNumber++) {
                IBotCommand command = commandManagerInstance.getCommand(commandListArray[commandNumber]);

                embedBuilder.addField(command.getName(), command.getDescription(), false);
            }
            embedBuilder.setFooter("Seite " + (page + 1) + " von " + (maxPage + 1) + ". ("+ commandPrefix + getName() +" [Seite])");
        }
        embedBuilder.setDescription(description);
        Helper.sendEmbed(embedBuilder, message, false);
    }

    @Override
    public String getName() {
        return "cinfo";
    }

    @Override
    public String getDescription() {
        return "`cinfo`: Information eines bestimmten Befehls oder von allen Befehlen";
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