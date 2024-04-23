package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;
import java.sql.SQLException;

public class MemberRecordCommand implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args)
            throws IOException, SQLException {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Rekord an Anzahl der Member";
        final String description = "Der aktuelle Rekord liegt bei " + "**" + QueryHelper.getMemberRecord() + "**"
                + " Membern";

        Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.RECORD);
        Helper.sendEmbed(embedBuilder, message, true);
    }

    @Override
    public String getName() {
        return "mr";
    }

    @Override
    public String getDescription() {
        return "`mr`: Rekord an Anzahl der Member auf " + Config.getInstance().getServerName();
    }

    @Override
    public boolean isPermitted(Member member) {
        return true;
    }

    @Override
    public String getHelpList() {
        return "stats";
    }
}