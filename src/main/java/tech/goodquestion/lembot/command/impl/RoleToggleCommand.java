package tech.goodquestion.lembot.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.BotCommand;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.lib.Helper;

import java.awt.*;

public class RoleToggleCommand implements BotCommand {

    public static void register(CommandManager cmds, String roleAbbr, long roleId) {
        cmds.registerCommand(new RoleToggleCommand(roleAbbr, roleId, Mode.ADD));
        cmds.registerCommand(new RoleToggleCommand(roleAbbr, roleId, Mode.REMOVE));
    }

    private final String roleAbbr;
    private final long roleId;
    private final Mode mode;

    public RoleToggleCommand(String roleAbbr, long roleId, Mode mode) {
        this.roleAbbr = roleAbbr;
        this.roleId = roleId;
        this.mode = mode;
    }

    @Override
    public void dispatch(Message msg, TextChannel channel, Member sender, String[] args) {
        Role role = msg.getGuild().getRoleById(roleId);
        String embedDescription;
        EmbedBuilder roleAddedEmbed = new EmbedBuilder();

        if (mode == Mode.ADD) {
            msg.getGuild().addRoleToMember(msg.getMember().getIdLong(), role).complete();
            embedDescription = "<@&" + roleId + "> wurde dir erfolgreich zugewiesen " + msg.getAuthor().getAsMention();
        } else {
            msg.getGuild().removeRoleFromMember(msg.getMember().getIdLong(), role).complete();
            embedDescription = "<@&" + roleId + "> wurde dir erfolgreich entfernt " + msg.getAuthor().getAsMention();
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
        return mode.getSymbol() + roleAbbr;
    }

    @Override
    public String getDescription() {
        if (mode == Mode.ADD) {
            return "`?" + getName() + "`: Weist dir <@&" + roleId + "> zu";
        } else {
            return "`?" + getName() + "`: Entfernt dir <@&" + roleId + ">";
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
