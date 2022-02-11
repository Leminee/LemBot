package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

public class NextBumpTimeCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        try {

            final String nextBumpTime = String.valueOf(QueryHelper.getNextBumpTime()).substring(0,5);
            int minutesBeforeNextBump = QueryHelper.getMinutesToNextBump() + 1;

            if (minutesBeforeNextBump < 0) {
                minutesBeforeNextBump = -1;
            }

            final String title = "Uhrzeit nächsten Bumps";
            final String description =  "Nächster Bump um **" + nextBumpTime
                    + "** " + "Uhr " + "(in **" + minutesBeforeNextBump + "** Minuten) ";
            final EmbedBuilder embedBuilder = new EmbedBuilder();

            Helper.createEmbed(embedBuilder,title,description, EmbedColorHelper.BUMP);
            Helper.sendEmbed(embedBuilder,message,true);

        } catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {

            System.out.println(stringIndexOutOfBoundsException.getMessage());
        }
    }

    @Override
    public String getName() {
        return "nbt";
    }

    @Override
    public String getDescription() {
        return "`?nbt`: Uhrzeit des nächsten Bumps";
    }

    @Override
    public boolean isPermitted(final Member member){
        return true;
    }
}
