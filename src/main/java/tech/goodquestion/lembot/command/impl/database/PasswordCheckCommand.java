package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PasswordCheckCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.length != 1) {
            return;
        }

        message.delete().queue();
        
        final String userPassword = args[0];
        final String embedTitle = "Passwort-Sicherheitsüberprüfung";

        if (hasBeenLeaked(userPassword)) {

            String description = ":red_circle: Passwort wurde leider gefunden " + message.getAuthor().getAsMention();

            Helper.createEmbed(embedBuilder, embedTitle, description, EmbedColorHelper.ERROR);

        } else {

            String description = ":green_circle: Nicht gefunden " +message.getAuthor().getAsMention();

            Helper.createEmbed(embedBuilder, embedTitle,description, EmbedColorHelper.SUCCESS);
        }

        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    private boolean hasBeenLeaked(final String userPassword) {

        if (userPassword.length() < 8) {
            return true;
        }


        final String bRockYouApiUrl = " https://goodquestion.tech:8443/brockyou/api/v2/" + userPassword;

        try {

            final URL url = new URL(bRockYouApiUrl);

            final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();


            return true;


        } catch (IOException ioException) {

            System.out.println(ioException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(ioException, this.getClass().getName()));
        }

        return false;
    }

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String getDescription() {
        return "`?check <password>`: Passwort-Sicherheitsüberprüfung";
    }
}
