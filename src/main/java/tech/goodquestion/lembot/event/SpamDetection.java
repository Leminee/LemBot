package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import tech.goodquestion.lembot.database.QueryHelper;

import java.util.List;
import java.util.Objects;

public class SpamDetection extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        long userId = event.getMessage().getAuthor().getIdLong();
        boolean senderIsBot = event.getMessage().getAuthor().isBot();
        boolean senderIsStaff = Objects.requireNonNull(event.getMessage().getMember()).hasPermission(Permission.MESSAGE_MANAGE);
        Member member = event.getMember();
        String userAsMention = event.getMessage().getAuthor().getAsMention();
        String messageContent = event.getMessage().getContentRaw();
        assert member != null;
        List<Role> userRoles = member.getRoles();
        Role mutedRole = event.getGuild().getRoleById(879329567947489352L);
        String staffRoleAsMention = Objects.requireNonNull(event.getGuild().getRoleById(784840290431008809L)).getAsMention();


        if (senderIsBot || senderIsStaff) return;

        if (QueryHelper.isSpammer(userId,messageContent)) {


            QueryHelper.deleteSpammersMessages(event,userId,messageContent);

            for (Role role : userRoles) {

                event.getGuild().removeRoleFromMember(userId, role).queue();
            }

            assert mutedRole != null;
            event.getGuild().addRoleToMember(userId,mutedRole).queue();

            event.getChannel().sendMessage("Du wurdest aufgrund verdächtigem Verhalten durch den Bot **gemutet**." + " \n" +
                    "Bitte kontaktiere einen [Staff] zwecks Überprüfung ".replace("[Staff]", staffRoleAsMention) + userAsMention +"!").queue();

        }
    }
}
