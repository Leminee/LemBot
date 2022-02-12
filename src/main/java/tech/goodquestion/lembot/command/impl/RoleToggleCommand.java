package tech.goodquestion.lembot.command.impl;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.command.IBotCommand;
import tech.goodquestion.lembot.library.EmbedColorHelper;
import tech.goodquestion.lembot.library.Helper;

import java.util.Objects;

public record RoleToggleCommand(String roleAbbr, long roleId,
                                tech.goodquestion.lembot.command.impl.RoleToggleCommand.Mode mode) implements IBotCommand {

    public static void register(CommandManager commandManager, String roleAbbr, long roleId) {
        commandManager.registerCommand(new RoleToggleCommand(roleAbbr, roleId, Mode.ADD));
        commandManager.registerCommand(new RoleToggleCommand(roleAbbr, roleId, Mode.REMOVE));
    }


    @Override
    public void dispatch(Message message, TextChannel channel, Member sender, String[] args) {
        Role role = message.getGuild().getRoleById(roleId);
        String embedDescription;
        EmbedBuilder embedBuilder = new EmbedBuilder();

        assert role != null;
        if (mode == Mode.ADD) {
            message.getGuild().addRoleToMember(Objects.requireNonNull(message.getMember()).getIdLong(), role).complete();
            embedDescription = "<@&" + roleId + "> wurde dir erfolgreich zugewiesen ";
        } else {
            message.getGuild().removeRoleFromMember(Objects.requireNonNull(message.getMember()).getIdLong(), role).complete();
            embedDescription = "<@&" + roleId + "> wurde dir erfolgreich entfernt ";
        }

        Helper.createEmbed(embedBuilder, "Bestätigung", embedDescription, EmbedColorHelper.SUCCESS);
        Helper.sendEmbed(embedBuilder,message,true);
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
            return "`" + getName() + ":` Weist dir <@&" + roleId + "> zu";
        } else {
            return "`" + getName() + ":` Entfernt dir <@&" + roleId + ">";
        }
    }

    @Override
    public boolean isPermitted(final Member member){
        return true;
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
