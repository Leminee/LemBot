package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.database.QueryHelper;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;
import java.time.Instant;

public class AdvertisingCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {

        if (args.length != 1) {

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = "Command nicht valid";
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        if (channel.getIdLong() != Config.getInstance().getChannelConfig().getStaffRoomChannel().getIdLong()) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Dieser Befehl kann nur in [channel] ausgeführt werden!".replace("[channel]", Config.getInstance().getChannelConfig().getStaffRoomChannel().getAsMention());
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        if (QueryHelper.hasAlreadyReceivedAdvertising(Long.parseLong(args[0]))) {

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String title = "Fehler";
            final String description = ":x: User hat schon eine Werbung erhalten";

            Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        try {

            advertiseServer(args);

        } catch (ErrorResponseException errorResponseException) {
            System.out.println(errorResponseException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(errorResponseException, this.getName()));

            final EmbedBuilder embedBuilder1 = new EmbedBuilder();

            Helper.createEmbed(embedBuilder1, "Fehler", ":x: DM konnte nicht zugestellt werden", EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder1, message, true);
        }
    }

    private void advertiseServer(String[] args) {

        final User user = Config.getInstance().getGuild().getJDA().retrieveUserById(args[0]).complete();

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Bewerte unseren Server";
        final String description = "Wir hoffen, dass wir Dir behilflich sein konnten und sein werden, und freuen uns darüber, dass Du Teil der Community bist!\n" +
                "Wir würden uns sehr freuen, wenn Du unseren Server auf **Disboard.org** (Webseite, die Server und Personen verbindet) bewertest.";
        final String embedColor = EmbedColorHelper.SERVER;
        embedBuilder.addField("Geschätzte Ausfüllzeit", "1 bis 2 Minuten", true);

        embedBuilder.addField("Link zur Bewertungsseite", "https://disboard.org/de/server/779105997792083969", true);
        embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/919074434021736507/920552764784914472/logoqg1_1.gif");
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/942585447383728159/942688214278369320/Bildschirmfoto_2022-02-14_um_08.46.20.png");
        embedBuilder.setFooter("Danke <3");
        embedBuilder.setTimestamp(Instant.now());

        Helper.createEmbed(embedBuilder, title, description, embedColor);

        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()))
                .complete();


        final String userTag = Config.getInstance().getGuild().getJDA().retrieveUserById(args[0]).complete().getAsTag();

        CommandHelper.logAdvertising(Long.parseLong(args[0]), userTag);
    }

    @Override
    public String getName() {
        return "ad";
    }

    @Override
    public String getDescription() {
        return "`ad <user id>`: Schickt dem User Werbung";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
