package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.lib.Helper;

import java.awt.*;

public class JoiningDateCommand implements IBotCommand {


    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        EmbedBuilder joiningDateEmbed = new EmbedBuilder();

        Helper.createEmbed(joiningDateEmbed, "Last Joining Date", "Du bist dem Server am " + "**" + getDateJoined(sender) + "**" + " beigetreten " + sender.getAsMention(), Color.gray);

        channel.sendMessage(joiningDateEmbed.build()).queue();

    }

    @Override
    public String getName() {
        return "jd";
    }

    @Override
    public String getDescription() {
        return "`?jd`: Zeigt das Beitrittsdatum zum Server an";
    }

    private static String getDateJoined(Member sender) {


        String day = String.valueOf(sender.getTimeJoined().getDayOfMonth());
        String month = String.valueOf(sender.getTimeJoined().getMonthValue());
        String year = String.valueOf(sender.getTimeJoined().getYear());

        return day + "-" + month + "-" + year;

    }

}
