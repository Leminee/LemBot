package tech.goodquestion.lembot.command.impl.db;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.BotCommand;
import tech.goodquestion.lembot.database.QueryHelper;

import java.awt.*;
import java.sql.SQLException;

public class TopActiveChannelsCommand implements BotCommand {

    @Override
    public void dispatch(Message msg, TextChannel channel, Member sender, String[] args) {
        try {
            MessageEmbed embed = QueryHelper.getTopActiveChannels()
                    .setColor(Color.gray)
                    .setTitle("Die aktivsten Channels")
                    .setThumbnail("https://cdn.discordapp.com/attachments/819694809765380146/872673996280303616/Bildschirmfoto_2021-08-05_um_04.54.26.png")
                    .build();
            channel.sendMessage(embed).queue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "topc";
    }

    @Override
    public String getDescription() {
        return "`?topc`: Zeigt die aktivsten Channels des Servers an";
    }
}
