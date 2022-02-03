package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.restlet.resource.ClientResource;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;

public class PasswordCheckCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

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


        final String bRockYouApiUrl = "https://goodquestion.tech:8443/brockyou/api/v2/" + userPassword;

        try {

            ClientResource resource = new ClientResource(bRockYouApiUrl);

            String apiResponseContent = resource.get().getText().toLowerCase();

            if (apiResponseContent.contains("\"hasBeenLeaked\":true".toLowerCase())) return true;


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

    @Override
    public boolean isPermitted(final Member member){
        return true;
    }
}
