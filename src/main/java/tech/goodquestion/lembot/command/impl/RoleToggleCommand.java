package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.lib.Helper;

import java.awt.*;

public class RoleToggleCommand implements IBotCommand {

    public static void register(CommandManager cmds, String roleAbbr, long roleId) {
        cmds.registerCommand(new RoleToggleCommand(roleAbbr, roleId, Mode.ADD));
        cmds.registerCommand(new RoleToggleCommand(roleAbbr, roleId, Mode.REMOVE));
    }

    private final String ROLE_ABBR;
    private final long ROLE_ID;
    private final Mode MODE;

    public RoleToggleCommand(String roleAbbr, long roleId, Mode mode) {
        this.ROLE_ABBR = roleAbbr;
        this.ROLE_ID = roleId;
        this.MODE = mode;
    }

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        Role role = message.getGuild().getRoleById(ROLE_ID);
        String embedDescription;
        EmbedBuilder roleAddedEmbed = new EmbedBuilder();

        if (MODE == Mode.ADD) {
            message.getGuild().addRoleToMember(message.getMember().getIdLong(), role).complete();
            embedDescription = "<@&" + ROLE_ID + "> wurde dir erfolgreich zugewiesen " + message.getAuthor().getAsMention();
        } else {
            message.getGuild().removeRoleFromMember(message.getMember().getIdLong(), role).complete();
            embedDescription = "<@&" + ROLE_ID + "> wurde dir erfolgreich entfernt " + message.getAuthor().getAsMention();
        }

        Helper.createEmbed(roleAddedEmbed, "Bestätigung", embedDescription, Color.GREEN);
        channel.sendMessage(roleAddedEmbed.build()).queue();
    }

    @Override
    public String getHelpList() {
        return "roles";
    }

    @Override
    public String getName() {
        return MODE.getSymbol() + ROLE_ABBR;
    }

    @Override
    public String getDescription() {
        if (MODE == Mode.ADD) {
            return "`?" + getName() + "`: Weist dir <@&" + ROLE_ID + "> zu";
        } else {
            return "`?" + getName() + "`: Entfernt dir <@&" + ROLE_ID + ">";
        }
    }

    public enum Mode {
        ADD("+"),
        REMOVE("-");

        private final String symbol;

        Mode(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
