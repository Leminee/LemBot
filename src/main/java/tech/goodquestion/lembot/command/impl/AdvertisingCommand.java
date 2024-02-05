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
import java.util.Objects;

public final class AdvertisingCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) throws IOException {

        if (args.length != 1) {


            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = "Command nicht valid";
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        if (channel.getIdLong() != Config.getInstance().getChannelConfig().getStaffCommandsChannel().getIdLong()) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String embedDescription = ":x: Dieser Befehl kann nur in [channel] ausgeführt werden!".replace("[channel]", Config.getInstance().getChannelConfig().getStaffCommandsChannel().getAsMention());
            Helper.createEmbed(embedBuilder, "Fehler", embedDescription, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        final User user = Helper.getUserFromCommandInput(message, args);

        if (QueryHelper.hasAlreadyReceivedAdvertising(user.getIdLong())) {

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String title = "Fehler";
            final String description = ":x: User hat schon eine Werbung erhalten";

            Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder, message, true);
            return;
        }

        try {

            advertiseServer(args, message);

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            final String title = "Bestätigung";
            final String description = ":white_check_mark: DM zugestellt";

            Helper.createEmbed(embedBuilder, title, description, EmbedColorHelper.SUCCESS);
            Helper.sendEmbed(embedBuilder, message, true);

        } catch (ErrorResponseException errorResponseException) {
            System.out.println(errorResponseException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(errorResponseException, this.getClass().getName()));

            final EmbedBuilder embedBuilder1 = new EmbedBuilder();

            Helper.createEmbed(embedBuilder1, "Fehler", ":x: DM konnte nicht zugestellt werden", EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder1, message, true);
        }
    }

    @SuppressWarnings("null")
    private void advertiseServer(String[] args, Message message) {

        final User user = Helper.getUserFromCommandInput(message,args);

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final String title = "Bewerte unseren Server";
        final String description = "Wir hoffen, dass wir Dir behilflich sein konnten und sein werden, und freuen uns darüber, dass Du Teil der Community bist!\n" +
                "Wir würden uns freuen, wenn Du unseren Server auf [**disboard.org**](https://disboard.org/de/server/779105997792083969) (Webseite, die Server und Personen verbindet) bewertest.";
        final String embedColor = EmbedColorHelper.SERVER;
        embedBuilder.addField("Geschätzte Ausfüllzeit", "1 bis 3 Minuten", true);

        embedBuilder.addField("Link zur Bewertungsseite", "[**disboard.org/gq**](https://disboard.org/de/review/create/779105997792083969)", true);
        embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/919074434021736507/920552764784914472/logoqg1_1.gif");
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/942585447383728159/942688214278369320/Bildschirmfoto_2022-02-14_um_08.46.20.png");
        embedBuilder.addField("Hinweis", "```Du musst auf disboard.org mit Deinem Discord-Account eingeloggt sein, um den Server bewerten zu können.```", false);
        embedBuilder.setFooter(Objects.requireNonNull(Config.getInstance().getGuild().getOwner()).getUser().getAsTag() +" | Danke <3",Config.getInstance().getGuild().getOwner().getUser().getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());

        Helper.createEmbed(embedBuilder, title, description, embedColor);

        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()))
                .complete();

        final String userTag = user.getAsTag();

        CommandHelper.logAdvertising(Long.parseLong(args[0]), userTag);
    }

    @Override
    public String getName() {
        return "ad";
    }

    @Override
    public String getDescription() {
        return "`ad <member>`: Schickt dem User Werbung";
    }

    @Override
    public String getHelpList() {
        return "staff";
    }
}
