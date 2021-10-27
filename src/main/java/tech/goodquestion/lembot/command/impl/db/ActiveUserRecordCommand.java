package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.lib.Helper;

import java.awt.*;

public class ActiveUserRecordCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        EmbedBuilder recordEmbed = new EmbedBuilder();
        Helper.createEmbed(recordEmbed, "Rekord an gleichzeitig aktiven Usern", "Der aktuelle Record liegt bei " + "**" + QueryHelper.getActiveUserRecord() + "**" + " gleichzeitig aktiven Usern " + sender.getAsMention(), Color.magenta);
        channel.sendMessage(recordEmbed.build()).queue();
    }

    @Override
    public String getName() {
        return "aur";
    }

    @Override
    public String getDescription() {
        return "`?aur`: Active User Record";
    }
}
