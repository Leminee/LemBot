package discord.bot.gq;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.List;
import java.util.Objects;

public class RoleSystem extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String userMessage = event.getMessage().getContentRaw();
        List<Role> userRoleList = Objects.requireNonNull(event.getMember()).getRoles();
        List<Role> serverRoleList = Objects.requireNonNull(BotMain.jda.getRoles());
        EmbedBuilder showRoles = new EmbedBuilder();

        if (userMessage.equalsIgnoreCase(BotMain.PREFIX + "role")) {
            if (!event.getMember().getUser().isBot()) {
                showRoles.setTitle("Rollen");
                showRoles.setColor(0x002d47);
                showRoles.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");
                StringBuilder sb = new StringBuilder();
                for (Role role : userRoleList) {
                    sb.append(role.getAsMention()).append("\n");
                }
                showRoles.setDescription("Du hast folgende Rollen: \n" + "\n" +
                        "-------------------- **Deine Rollen** -------------------- \n" + "\n" + sb);
                event.getChannel().sendMessage(showRoles.build()).queue();
            }
        }
        if (userMessage.equalsIgnoreCase(BotMain.PREFIX + "srole")) {
            if (!event.getMember().getUser().isBot()) {
                showRoles.setTitle("Liste aller Rollen auf GQ");
                showRoles.setColor(0x002d47);
                showRoles.setThumbnail("https://cotelangues.com/wp-content/uploads/2019/06/Fragezeichen-Tafel-868x524.jpg");
                StringBuilder sb = new StringBuilder();
                for (Role role : serverRoleList) {
                    sb.append(role.getAsMention()).append("\n");
                }
                showRoles.setDescription("Der Server vergibt folgende Rollen: \n " + "\n" +
                        "-------------------- **Rollen** -------------------- \n" + "\n" + sb);
                event.getChannel().sendMessage(showRoles.build()).queue();
            }
        }
    }
}
