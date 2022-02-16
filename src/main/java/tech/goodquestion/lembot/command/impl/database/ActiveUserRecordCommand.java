package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

public final class ActiveUserRecordCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Rekord an gleichzeitig aktiven Usern";
        final String description = "Der aktuelle Record liegt bei " + "**" + QueryHelper.getActiveUserRecord() + "**" + " gleichzeitig aktiven Usern";

        Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.RECORD);

        Helper.sendEmbed(embedBuilder, message, true);
    }

    @Override
    public String getName() {
        return "aur";
    }

    @Override
    public String getDescription() {
        return "`aur`: Record an gleichzeitig aktiven Usern";
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }

    @Override
    public String getHelpList() {
        return "stats";
    }
}