package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.lib.Helper;

import java.awt.*;

public class JoiningDate implements IBotCommand {


    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        EmbedBuilder recordEmbed = new EmbedBuilder();
        Helper.createEmbed(recordEmbed, "JoiningDate", "Du bist dem Server " + "**" + QueryHelper.getJoiningDate(sender.getIdLong()) + "**" + " beigetreten " + sender.getAsMention(), Color.gray);
        channel.sendMessage(recordEmbed.build()).queue();
    }

    @Override
    public String getName() {
        return "jd";
    }

    @Override
    public String getDescription() {
        return "`?jd`: Zeigt das Beitrittsdatum zum Server an";
    }
}
