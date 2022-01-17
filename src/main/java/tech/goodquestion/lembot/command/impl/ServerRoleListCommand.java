package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.library.EmbedColorHelper;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class ServerRoleListCommand implements IBotCommand {

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {

        final EmbedBuilder showRoles = new EmbedBuilder();
        final List<Role> serverRoleList = Objects.requireNonNull(message.getGuild().getRoles());

        showRoles.setTitle("Liste aller Rollen auf GQ");
        showRoles.setColor(Color.decode(EmbedColorHelper.SERVER_ROLES));

        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < serverRoleList.size() - 2; i++) {
            stringBuilder.append(serverRoleList.get(i).getAsMention()).append("\n");
        }

        showRoles.setDescription("Der Server vergibt folgende Rollen: \n " + "\n" +
                "-------------------- **Rollen** -------------------- \n" + "\n" + stringBuilder);
        channel.sendMessage(showRoles.build()).queue();
    }

    @Override
    public String getName() {
        return "sroles";
    }

    @Override
    public String getDescription() {
        return "`?sroles`: Liste aller Rollen auf GQ";
    }
}
