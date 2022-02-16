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

public final class ServerRoleListCommand implements IBotCommand {

    @Override
    public void dispatch(final Message message, final TextChannel channel, final Member sender, final String[] args) {

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final List<Role> serverRoles = Objects.requireNonNull(message.getGuild().getRoles());

        embedBuilder.setTitle(String.format("Liste aller %s Rollen auf " + Config.getInstance().getServerName(), serverRoles.size()));
        embedBuilder.setColor(Color.decode(EmbedColorHelper.SERVER_ROLES));

        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < serverRoles.size() - 2; i++) {
            stringBuilder.append(serverRoles.get(i).getAsMention()).append("\n");
        }

        embedBuilder.setDescription(stringBuilder);
        Helper.sendEmbed(embedBuilder, message, true);
    }

    @Override
    public String getName() {
        return "sroles";
    }

    @Override
    public String getDescription() {
        return "`sroles`: Liste aller Rollen auf " + Config.getInstance().getServerName();
    }

    @Override
    public boolean isPermitted(final Member member) {
        return true;
    }
}
