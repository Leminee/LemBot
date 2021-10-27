package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.BotCommand;
import tech.goodquestion.lembot.database.QueryHelper;

public class NextBumpTimeCommand implements BotCommand {

    @Override
    public void dispatch(Message msg, TextChannel channel, Member sender, String[] args) {
        try {
            channel.sendMessage("Nächster Bump um **" + String.valueOf(QueryHelper.getNextBumpTime()).substring(0,5)
                    + "** " + "Uhr " + "(in **" + (QueryHelper.getMinutesToNextBump() + 1) + "** Minuten) " + msg.getAuthor().getAsMention()).queue();
        } catch (StringIndexOutOfBoundsException exc) {
            channel.sendMessage("Nächter Bump unbekannt! " + sender.getAsMention()).queue();
            exc.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "nbt";
    }

    @Override
    public String getDescription() {
        return "`?nbt`: Zeigt an, wann das nächte mal gebumped werden kann";
    }
}
