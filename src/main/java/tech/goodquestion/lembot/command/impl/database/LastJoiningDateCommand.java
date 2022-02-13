package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;
import tech.goodquestion.lembot.library.parser.LocalDateTimeFormatter;

public class LastJoiningDateCommand implements IBotCommand {


    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Letztes Beitrittsdatum";
        final String description = "Du bist dem Server am " + "**" + LocalDateTimeFormatter.toGermanFormat(sender.getTimeJoined().toLocalDateTime()) + "**" + " beigetreten ";

        Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.JOIN_DATE);

        Helper.sendEmbed(embedBuilder,message,true);

    }

    @Override
    public String getName() {
        return "ljd";
    }

    @Override
    public String getDescription() {
        return "`ljd`: Letztes Beitrittsdatum zum Server";
    }

    @Override
    public boolean isPermitted(final Member member){
        return true;
    }

    @Override
    public String getHelpList() {
        return "stats";
    }

}
