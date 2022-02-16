package tech.goodquestion.lembot.command.impl.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONObject;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.database.CommandHelper;
import tech.goodquestion.lembot.entity.OccurredException;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.io.IOException;

public final class PasswordCheckCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();

        if (args.length != 1) {
            return;
        }

        delete(message);

        final String password = args[0];
        final String embedTitle = "Passwort-Sicherheitsüberprüfung";


        try {
            if (hasBeenLeaked(password)) {

                String description = ":red_circle: Passwort wurde leider gefunden ";

                Helper.createEmbed(embedBuilder, embedTitle, description, EmbedColorHelper.ERROR);

            } else {

                String description = ":green_circle: Nicht gefunden ";

                Helper.createEmbed(embedBuilder, embedTitle, description, EmbedColorHelper.SUCCESS);
            }

            Helper.sendEmbed(embedBuilder, message, true);

        } catch (ResourceException resourceException) {

            final EmbedBuilder embedBuilder1 = new EmbedBuilder();
            Helper.createEmbed(embedBuilder1, "Error", "API outage :(", EmbedColorHelper.ERROR);
            Helper.sendEmbed(embedBuilder1,message,true);
        }
    }

    private boolean hasBeenLeaked(final String password) {

        if (password.length() < 8) {
            return true;
        }

        final String bRockYouApiUrl = "https://goodquestion.tech:8443/brockyou/api/v2/" + password;

        try {

            final ClientResource resource = new ClientResource(bRockYouApiUrl);
            final String apiResponseContent = resource.get().getText();
            final JSONObject jsonObject = new JSONObject(apiResponseContent);
            final boolean hasBeenLeaked = (boolean) jsonObject.get("hasBeenLeaked");

            if (hasBeenLeaked) return true;

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            CommandHelper.logException(OccurredException.getOccurredExceptionData(ioException, this.getClass().getName()));
        }

        return false;
    }

    private void delete(Message message){
        message.delete().queue();
    }

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String getDescription() {
        return "`check <password>`: Passwort-Sicherheitsüberprüfung";
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }
}
