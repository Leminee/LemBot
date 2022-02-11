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
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Letztes Beitrittsdatum";
        final String description = "Du bist dem Server am " + "**" + getDateJoined(sender) + "**" + " beigetreten ";

        Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.JOIN_DATE);

        Helper.sendEmbed(embedBuilder,message,true);

    }

    @Override
    public String getName() {
        return "ljd";
    }

    @Override
    public String getDescription() {
        return "`?ljd`: Letztes Beitrittsdatum zum Server";
    }

    @Override
    public boolean isPermitted(final Member member){
        return true;
    }

    private String getDateJoined(final Member sender) {


        String day = String.valueOf(sender.getTimeJoined().getDayOfMonth());
        String month = String.valueOf(sender.getTimeJoined().getMonthValue());
        String year = String.valueOf(sender.getTimeJoined().getYear());

        return day + "-" + month + "-" + year;

    }

}
