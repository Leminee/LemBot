package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public final class ServerRoleListInfoCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {


        final String[] rolesInfo = {"Admin ist zugleich ein Moderator und hat Administrationsrechte.", "Der Server-Bot", "Moderator hat unter anderem die Berechtigung, Rollen zuzuweisen, diese zu entfernen und Mitglieder zu sanktionieren, die gegen die in ⁠rules aufgeführten Regeln verstoßen.", "Helper ist jeder, der die Grundlagen einer Programmiersprache beherrscht und somit in der Lage ist, anderen zu helfen - und ihnen hilft.", "User, die sich diese Rolle selbst zugewiesen haben, um beim Bumpen des Servers mit zu helfen.", "User, die den Server geboostet haben.", "<@815894805896888362>-Developper mit mindestens 2 Commits.", "User, die ein Rätsel gelöst haben", "User, die Programmierer sind oder Programmiersprachen lernen.", "...sind Discord-Bots. Wer hätte es gedacht?"};

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final List<Role> serverRoles = Objects.requireNonNull(message.getGuild().getRoles());

        embedBuilder.setTitle(String.format("Informationen zu allen Rollen auf " + Config.getInstance().getServerName()));
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SERVER_ROLES));

        final StringBuilder stringBuilder = new StringBuilder();

        final int amountServerRoles = serverRoles.size();
        final int amountNonVisibleRoles = 13;

        for (int i = 0; i < amountServerRoles - amountNonVisibleRoles; i++) {
            stringBuilder.append(serverRoles.get(i).getAsMention()).append(": ").append(rolesInfo[i]).append("\n").append("\n");
        }

        embedBuilder.setDescription(stringBuilder);
        Helper.sendEmbed(embedBuilder, message, true);
    }


    @Override
    public String getName() {
        return "sri";
    }

    @Override
    public String getDescription() {
        return "`sri`: Informationen zu allen Rollen auf " + Config.getInstance().getServerName();
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }
}