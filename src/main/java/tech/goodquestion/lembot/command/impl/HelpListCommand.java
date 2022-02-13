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
import java.util.List;
import java.util.*;

public class HelpListCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final char prefix = Config.getInstance().getBotConfig().getPrefix();


        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("HILFE \n");
        embedBuilder.setColor(Color.decode(EmbedColorHelper.HELP));
        embedBuilder.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");

        String queriedHelpList = "";

        if (args.length > 0) {
            queriedHelpList = args[0].toLowerCase(Locale.ROOT);
        }

        if (queriedHelpList.equals("") ||queriedHelpList.equals("-")) {
            EmbedBuilder embedBuilder1 = new EmbedBuilder().setColor(Color.decode(EmbedColorHelper.HELP))
                    .setTitle("Verfügbare Help-Listen")
                    .setDescription(prefix+ "help " + String.join("\n" +prefix+"help ",CommandManager.getInstance().getHelpLists()));

            Helper.sendEmbed(embedBuilder1,message,true);
            return;
        }

        StringBuilder descriptionBuilder = new StringBuilder("Hallo, ich heiße **" + Config.getInstance().getBotConfig().getName()  + "** und bin ein Bot für **"+ Config.getInstance().getServerName() +  "** :)\n");
        descriptionBuilder.append("\n---------------    **BEFEHLSLISTE**    ---------------\n");
        descriptionBuilder.append("\n");

        List<IBotCommand> commandsOnHelpList = new ArrayList<>();

        for (IBotCommand command : CommandManager.getInstance().getCommands().values()) {
            if (!Objects.equals(command.getHelpList(), queriedHelpList)) continue;
            commandsOnHelpList.add(command);

        }
        
        commandsOnHelpList.sort(Comparator.comparing(IBotCommand::getName));
        commandsOnHelpList.forEach(c -> descriptionBuilder.append(prefix).append(c.getDescription()).append("\n"));

        embedBuilder.setDescription(descriptionBuilder.toString());
        Helper.sendEmbed(embedBuilder, message,true);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "`help -`: Verfügbare Command-Listen";
    }

    @Override
    public boolean isPermitted(final Member member){
        return true;
    }

    @Override
    public String getHelpList() {
        return "general";
    }

}
