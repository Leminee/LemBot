package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class BotDataCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        final String bot = Objects.requireNonNull(Config.getInstance().getGuild().getMemberById(Config.getInstance().getBotConfig().getId())).getUser().getAsTag();
        final String gitHubRepositoryUrl = "https://github.com/Leminee/LemBot";
        final String botIconUrl = Objects.requireNonNull(Config.getInstance().getGuild().getMemberById(Config.getInstance().getBotConfig().getId())).getUser().getEffectiveAvatarUrl();
        final String botAuthor = Objects.requireNonNull(message.getGuild().getMemberById(739143338975952959L)).getAsMention();

        final EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor(bot, gitHubRepositoryUrl, botIconUrl).setTitle("Informationen zum " + Config.getInstance().getBotConfig().getName()).setColor(Color.decode(EmbedColorHelper.SERVER)).setThumbnail("https://cdn.discordapp.com/attachments/919074434021736507/920552764784914472/logoqg1_1.gif").addField("Geschrieben in", "Java (JDA)", true).addField("Geschrieben von", botAuthor, true).addField("Akutelle Version", Config.getInstance().getBotConfig().getVersion(), true).addField("**" + getAmountContributors() + "**" + " Mitwirkende", (QueryHelper.getContributorsAsMention() + "\n").replace("[", "").replace("]", "").replace(",", "\n"), true).addField("Source Code", gitHubRepositoryUrl, true);

        Helper.sendEmbed(embedBuilder, message, true);
    }


    private String getAmountContributors() {

        try {

            final Document document = Jsoup.connect("https://github.com/Leminee/LemBot").userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36").get();
            final Elements element = document.getElementsByClass("Counter");

            return element.get(element.size() - 1).text();

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
        return "-1";
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