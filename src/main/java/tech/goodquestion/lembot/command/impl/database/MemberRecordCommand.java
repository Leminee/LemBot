package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;

import java.io.IOException;
import java.sql.SQLException;

public class UserRecordCommand implements IBotCommand {
    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) throws IOException, SQLException {

    }

    @Override
    public String getName() {
        return "mr";
    }

    @Override
    public String getDescription() {
        return "`mr`: Rekord an Anzahl der Membern auf " + Config.getInstance().getServerName();
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
