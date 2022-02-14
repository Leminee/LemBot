package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BotsCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {

        final List<Member> members = Config.getInstance().getGuild().getMembers();

        final List<Member> bots = members
                .stream()
                .filter(member -> member.getUser().isBot())
                .collect(Collectors.toList());

        final StringBuilder stringBuilder = new StringBuilder();

        for (final Member bot : bots) {

            if (!bot.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {

                stringBuilder.append(":green_circle: ").append(bot.getUser().getAsMention()).append("\n");
            } else {
                stringBuilder.append(":black_circle: ").append(bot.getUser().getAsMention()).append("\n");
            }

        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        final String description = String.valueOf(stringBuilder);
        final String title = bots.size() + " Bots";

        Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.SERVER);
        Helper.sendEmbed(embedBuilder, message, true);

    }

    @Override
    public String getName() {
        return "sb";
    }

    @Override
    public String getDescription() {

        return "`sb`: Liste der Server-Bots";
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
