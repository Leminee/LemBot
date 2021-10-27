package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;

import java.util.List;
import java.util.Objects;

public class UserRoleListCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        EmbedBuilder showRoles = new EmbedBuilder();
        List<Role> userRoleList = Objects.requireNonNull(message.getMember()).getRoles();

        showRoles.setTitle("Rollen");
        showRoles.setColor(0x002d47);
        StringBuilder sb = new StringBuilder();
        for (Role role : userRoleList) {
            sb.append(role.getAsMention()).append("\n");
        }
        showRoles.setDescription("Du hast folgende Rolle(n): \n" + "\n" +
                "---------------- **Deine Rollen** ---------------- \n" + "\n" + sb);
        channel.sendMessage(showRoles.build()).queue();
    }

    @Override
    public String getName() {
        return "roles";
    }

    @Override
    public String getDescription() {
        return "`?roles`: Liste deiner Rollen";
    }
}
