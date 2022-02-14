package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.util.Objects;

public class BotDataCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        final String bot = Objects.requireNonNull(Config.getInstance().getGuild().getMemberById(Config.getInstance().getBotConfig().getId())).getUser().getAsTag();
        final String gitHubRepositoryUrl = "https://github.com/Leminee/LemBot";
        final String botIconUrl = Objects.requireNonNull(Config.getInstance().getGuild().getMemberById(Config.getInstance().getBotConfig().getId())).getUser().getEffectiveAvatarUrl();

        final EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(bot, gitHubRepositoryUrl, botIconUrl)
                .setTitle("Informationen zum " + Config.getInstance().getBotConfig().getName())
                .setColor(Color.decode(EmbedColorHelper.SERVER))
                .setThumbnail("https://cdn.discordapp.com/attachments/919074434021736507/920552764784914472/logoqg1_1.gif")
                .addField("Geschrieben in", "Java (JDA)", true)
                .addField("Geschrieben von", Objects.requireNonNull(Config.getInstance().getGuild().getMemberById(739143338975952959L)).getAsMention(), true)
                .addField("Akutelle Version", Config.getInstance().getBotConfig().getVersion(), true)
                .addField("Mitwirkende", Helper.getAmountContributors(gitHubRepositoryUrl), true)
                .addField("Source Code", gitHubRepositoryUrl, true);


        Helper.sendEmbed(embedBuilder, message, true);
    }

    @Override
    public String getName() {
        return Config.getInstance().getBotConfig().getName().toLowerCase();
    }

    @Override
    public String getDescription() {
        return "`" + Config.getInstance().getBotConfig().getName().toLowerCase() + "`: " + "Informationen zum " + Config.getInstance().getBotConfig().getName();
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }
}
