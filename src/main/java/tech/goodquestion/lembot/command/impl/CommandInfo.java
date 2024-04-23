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
import java.util.*;


public class CommandInfo implements IBotCommand {

    public final static CommandManager commandManagerInstance = CommandManager.getInstance();

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final char commandPrefix = Config.getInstance().getBotConfig().getPrefix();

        // Wird als Embed ausgegeben
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

        // Alle commands
        Map<String, IBotCommand> commandsMap = commandManagerInstance.getCommands();
        // Eine liste der Commands, auf die der User Zugriff hat
        Set<String> availableCommands = commandsMap.keySet();

        // Prüft bei jedem Command, ob der User diesen ausführen darf
        for(IBotCommand command : commandsMap.values()) {
            if (!command.isPermitted(sender)) {
                // Wenn der User keine Rechte für den Command hat, wird dieser aus dem Set entfernt
                availableCommands.remove(command.getName());
            }
        }


        if(args.length != 0 && isNotNumeric(args[0])) {
            // Die Information für einen bestimmten oder mehrere bestimmte Commands wird angefragt.
            try {
                availableCommands.removeIf(commandName -> !Arrays.asList(args).contains(commandName));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // Es sollen alle Commands auf mehrere Seiten verteilt werden
        int page;
        int commandsPerPage = 20;
        int minPage = 0;

        // In ein Array umgewandelt, damit die Commands einer bestimmten Seite angezeigt werden
        String[] availableCommandsArray = availableCommands.toArray(new String[0]);
        // Ceiling round (immer aufrunden)
        int maxPage = availableCommandsArray.length / commandsPerPage;

        // Prüft, ob eine gültige Seitenzahl vom User angegeben wird, wenn nicht, wird die erste Seite aufgerufen
        if(args.length == 0 || isNotNumeric(args[args.length - 1])) {
            page = 0;
        } else {
            page = Integer.parseInt(args[args.length-1]) - 1;
        }
        if(page < minPage || page > maxPage) {
            page = 0;
        }

        int firstCommandNumber = page * commandsPerPage;
        // Die Commands der Seite werden nacheinander ausgegeben
        for(int commandNumber = firstCommandNumber; commandNumber <= (firstCommandNumber + commandsPerPage) && commandNumber < availableCommandsArray.length; commandNumber++) {
            IBotCommand command = commandManagerInstance.getCommand(availableCommandsArray[commandNumber]);
            embedBuilder.addField(commandPrefix + command.getName(), command.getDescription(), false);
        }
        embedBuilder.setFooter("Seite " + (page + 1) + " von " + (maxPage + 1) + ". ("+availableCommands.size()+" Commands. Syntax: "+ commandPrefix + getName() +" [Command Filter] [Seite])");

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

    private static boolean isNotNumeric(String strNum) {
        if (strNum == null) {
            return true;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return true;
        }
        return false;
    }
}