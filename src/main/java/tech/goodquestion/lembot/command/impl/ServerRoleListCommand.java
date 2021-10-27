package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.BotCommand;

import java.util.List;
import java.util.Objects;

public class ServerRoleListCommand implements BotCommand {

    @Override
    public void dispatch(Message msg, TextChannel channel, Member sender, String[] args) {
        EmbedBuilder showRoles = new EmbedBuilder();
        List<Role> serverRoleList = Objects.requireNonNull(msg.getGuild().getRoles());

        showRoles.setTitle("Liste aller Rollen auf GQ");
        showRoles.setColor(0x002d47);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < serverRoleList.size() - 3; i++) {
            sb.append(serverRoleList.get(i).getAsMention()).append("\n");
        }

        showRoles.setDescription("Der Server vergibt folgende Rollen: \n " + "\n" +
                "-------------------- **Rollen** -------------------- \n" + "\n" + sb);
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
