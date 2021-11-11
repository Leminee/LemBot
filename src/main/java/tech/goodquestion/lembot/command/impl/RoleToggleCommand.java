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
import java.util.Objects;

public class RoleToggleCommand implements IBotCommand {

    public static void register(CommandManager cmds, String roleAbbr, long roleId) {
        cmds.registerCommand(new RoleToggleCommand(roleAbbr, roleId, Mode.ADD));
        cmds.registerCommand(new RoleToggleCommand(roleAbbr, roleId, Mode.REMOVE));
    }

    private final String role_abbr;
    private final long role_id;
    private final Mode mode;

    public RoleToggleCommand(String roleAbbr, long roleId, Mode mode) {
        this.role_abbr = roleAbbr;
        this.role_id = roleId;
        this.mode = mode;
    }

    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        Role role = message.getGuild().getRoleById(role_id);
        String embedDescription;
        EmbedBuilder roleAddedEmbed = new EmbedBuilder();

        assert role != null;
        if (mode == Mode.ADD) {
            message.getGuild().addRoleToMember(Objects.requireNonNull(message.getMember()).getIdLong(), role).complete();
            embedDescription = "<@&" + role_id + "> wurde dir erfolgreich zugewiesen " + message.getAuthor().getAsMention();
        } else {
            message.getGuild().removeRoleFromMember(Objects.requireNonNull(message.getMember()).getIdLong(), role).complete();
            embedDescription = "<@&" + role_id + "> wurde dir erfolgreich entfernt " + message.getAuthor().getAsMention();
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
        return mode.getSymbol() + role_abbr;
    }

    @Override
    public String getDescription() {
        if (mode == Mode.ADD) {
            return "`?" + getName() + "`: Weist dir <@&" + role_id + "> zu";
        } else {
            return "`?" + getName() + "`: Entfernt dir <@&" + role_id + ">";
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
