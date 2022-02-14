package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;
import java.time.Instant;

public class AdvertisingCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {


        if (channel.getIdLong() != Config.getInstance().getChannelConfig().getBumpChannel().getIdLong()) return;

        promoteServer(args);

    }

    private void promoteServer(String[] args) {

        final User user = Config.getInstance().getGuild().getJDA().retrieveUserById(args[0]).complete();

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Bewerte unseren Server";
        final String description = "Wir hoffen, dass wir Dir behilflich sein konnten und sein werden, und freuen uns darüber, dass Du Teil der Community bist!\n" +
                "Wir würden uns sehr freuen, wenn Du unseren Server auf **DisBoard.org** (Website, die Server und Personen verbindet) bewerten würdest.";
        final String embedColor = EmbedColorHelper.SERVER;
        embedBuilder.addField("Geschätzte Ausfüllzeit", "1 bis 2 Minuten", true);

        embedBuilder.addField("Url zu der Bewertungsseite", "https://disboard.org/de/server/779105997792083969", true);
        embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/919074434021736507/920552764784914472/logoqg1_1.gif");
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/942585447383728159/942688214278369320/Bildschirmfoto_2022-02-14_um_08.46.20.png");
        embedBuilder.setFooter("Danke <3");
        embedBuilder.setTimestamp(Instant.now());

        Helper.createEmbed(embedBuilder, title, description, embedColor);

        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()))
                .complete();
    }

    @Override
    public String getName() {
        return "ad";
    }

    @Override
    public String getDescription() {
        return "`ad <user> <delay>`: Schickt dem User Werbung";
    }

    @Override
    public String getHelpList() {
        return "general";
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }
}
