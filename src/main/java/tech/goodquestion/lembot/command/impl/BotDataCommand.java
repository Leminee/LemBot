package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.BotMain;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.util.Objects;

public class BotDataCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args){


        final String bot = Config.getInstance().getGuild().getMemberById(Config.getInstance().getUser().getLemBotId()).getUser().getAsTag();
        final String gitHubRepositoryUrl = "https://github.com/Leminee/LemBot";
        final String botIconUrl = Config.getInstance().getGuild().getMemberById(Config.getInstance().getUser().getLemBotId()).getUser().getEffectiveAvatarUrl();

        final EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(bot, gitHubRepositoryUrl, botIconUrl)
                .setTitle("Informationen zum LemBot")
                .setColor(Color.decode(EmbedColorHelper.GOOD_QUESTION))
                .setThumbnail("https://cdn.discordapp.com/attachments/919074434021736507/920552764784914472/logoqg1_1.gif")
                .addField("Geschrieben in", "Java (JDA)", true)
                .addField("Geschrieben von", Objects.requireNonNull(Config.getInstance().getGuild().getOwner()).getAsMention(), true)
                .addField("Akutelle Version", BotMain.BOT_VERSION, true)
                .addField("Mitwirkende", Helper.getAmountLemBotContributors(), true)
                .addField("Source Code", gitHubRepositoryUrl, true);


        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "lembot";
    }

    @Override
    public String getDescription() {
        return "`?lembot`: Informationen zum LemBot";
    }
}
