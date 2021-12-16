package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

public class JoiningDateCommand implements IBotCommand {


    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        EmbedBuilder joiningDateEmbed = new EmbedBuilder();

        Helper.createEmbed(joiningDateEmbed, "Letztes Beitrittsdatum", "Du bist dem Server am " + "**" + getDateJoined(sender) + "**" + " beigetreten " + sender.getAsMention(), EmbedColorHelper.JOIN_DATE);

        channel.sendMessage(joiningDateEmbed.build()).queue();

    }

    @Override
    public String getName() {
        return "ljd";
    }

    @Override
    public String getDescription() {
        return "`?ljd`: Letztes Beitrittsdatum zum Server";
    }

    private static String getDateJoined(Member sender) {


        String day = String.valueOf(sender.getTimeJoined().getDayOfMonth());
        String month = String.valueOf(sender.getTimeJoined().getMonthValue());
        String year = String.valueOf(sender.getTimeJoined().getYear());

        return day + "-" + month + "-" + year;

    }

}
