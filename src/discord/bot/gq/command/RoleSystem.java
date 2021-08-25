package discord.bot.gq.command;

import discord.bot.gq.lib.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Objects;

public class RoleSystem extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessageContent = event.getMessage().getContentRaw();
        List<Role> userRoleList = Objects.requireNonNull(event.getMember()).getRoles();
        List<Role> serverRoleList = Objects.requireNonNull(event.getGuild().getRoles());
        EmbedBuilder showRoles = new EmbedBuilder();

        if (userMessageContent.equalsIgnoreCase(Helper.PREFIX + "role")) {
            if (!event.getMember().getUser().isBot()) {
                showRoles.setTitle("Rollen");
                showRoles.setColor(0x002d47);
                StringBuilder sb = new StringBuilder();
                for (Role role : userRoleList) {
                    sb.append(role.getAsMention()).append("\n");
                }
                showRoles.setDescription("Du hast folgende Rolle(n): \n" + "\n" +
                        "---------------- **Deine Rollen** ---------------- \n" + "\n" + sb);
                event.getChannel().sendMessage(showRoles.build()).queue();
            }
        }

        if (userMessageContent.equalsIgnoreCase(Helper.PREFIX + "srole")) {

            if (!event.getMember().getUser().isBot()) {
                showRoles.setTitle("Liste aller Rollen auf GQ");
                showRoles.setColor(0x002d47);
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < serverRoleList.size() - 4; i++) {
                    sb.append(serverRoleList.get(i).getAsMention()).append("\n");
                }

                showRoles.setDescription("Der Server vergibt folgende Rollen: \n " + "\n" +
                        "-------------------- **Rollen** -------------------- \n" + "\n" + sb);
                event.getChannel().sendMessage(showRoles.build()).queue();
            }
        }
    }
}
