package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class ServerDataCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        final List<Member> memberList = message.getGuild().getMembers();
        final long amountBots = memberList.stream()
                .filter(member -> member.getUser().isBot())
                .count();


        embedBuilder.setTitle("Informationen zum Server")
                .setColor(Color.decode(EmbedColorHelper.GOOD_QUESTION))
                .setDescription("Informationen zu **GoodQuestion**")
                .setThumbnail("https://cdn.discordapp.com/attachments/919074434021736507/920552764784914472/logoqg1_1.gif")
                .addField("Servermitglieder", String.valueOf(Config.getInstance().getGuild().getMemberCount()), true)
                .addField("Erstellungsdatum", getTimeCreated(), true)
                .addField("Nachrichten", String.valueOf(QueryHelper.getMessagesCount()), true)
                .addField("Boosts", String.valueOf(Config.getInstance().getGuild().getBoostCount()), true)
                .addField("Bots", String.valueOf(amountBots), true)
                .addField("Einladungslink", "https://discord.gg/4YwafTCKGh", true)
                .addField("Rollen", String.valueOf(Config.getInstance().getGuild().getRoles().size()), true)
                .addField("Textkanäle", String.valueOf(Config.getInstance().getGuild().getTextChannels().size()), true)
                .addField("Sprachkanäle", String.valueOf(Config.getInstance().getGuild().getVoiceChannels().size()), true)
                .addField("Owner", Objects.requireNonNull(Config.getInstance().getGuild().getOwner()).getAsMention(), true)
                .addField("Administratoren", (QueryHelper.getAdminsAsMention() + "\n")
                        .replace("[", "")
                        .replace("]", "")
                        .replace(",", "\n"), true)
                .addField("Moderatoren", (QueryHelper.getModeratorsAsMention() + "\n")
                        .replace("[", "")
                        .replace("]", "")
                        .replace(",", "\n"), true);


        channel.sendMessageEmbeds(embedBuilder.build()).queue();


    }

    private String getTimeCreated() {

        return Config.getInstance().getGuild().getTimeCreated().getDayOfMonth() + "-"
                + Config.getInstance().getGuild().getTimeCreated().getMonthValue() + "-"
                + Config.getInstance().getGuild().getTimeCreated().getYear();
    }

    @Override
    public String getName() {
        return "server";
    }

    @Override
    public String getDescription() {
        return "`?server`: Informationen zum Server";
    }
}
