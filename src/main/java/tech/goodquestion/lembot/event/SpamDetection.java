package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.database.QueryHelper;

import java.util.Objects;

public class SpamDetection extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        long userId = event.getMessage().getAuthor().getIdLong();
        String userAsMention = event.getMessage().getAuthor().getAsMention();
        String messageContent = event.getMessage().getContentRaw();

        Role mutedRole = event.getGuild().getRoleById(879329567947489352L);
        String staffRoleAsMention  = Objects.requireNonNull(event.getGuild().getRoleById(784840290431008809L)).getAsMention();


        if(QueryHelper.isSpammer(userId,messageContent)) {

            assert mutedRole != null;
            event.getGuild().addRoleToMember(userId,mutedRole).queue();

            event.getChannel().sendMessage("Du wurdest aufgrund verdächtigem Verhalten **gemutet**" + " \n" +
                    "Bitte kontaktiere einen [Staff] zwecks Überprüfung ".replace("[Staff]", staffRoleAsMention) + userAsMention +"!").queue();

        }
    }
}
