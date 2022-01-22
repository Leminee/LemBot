package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

public class JoiningDateCommand implements IBotCommand {


    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Letztes Beitrittsdatum";
        final String description = "Du bist dem Server am " + "**" + getDateJoined(sender) + "**" + " beigetreten " + sender.getAsMention();

        Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.JOIN_DATE);

        channel.sendMessageEmbeds(embedBuilder.build()).queue();

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
