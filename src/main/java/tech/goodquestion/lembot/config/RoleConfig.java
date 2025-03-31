package tech.goodquestion.lembot.config;

import net.dv8tion.jda.api.entities.Role;

public final class RoleConfig {

    private long bump;
    private long mute;
    private long warn;
    private long coding;
    private long moderator;
    private long admin;

    public long getBumpRoleId() {
        return bump;
    }

    public Role getCodingRole() {
        return Config.getInstance().getGuild().getRoleById(coding);
    }

    public Role getMuteRole() {
        return Config.getInstance().getGuild().getRoleById(mute);
    }

    public Role getWarnRole() {
        return Config.getInstance().getGuild().getRoleById(warn);
    }

    public Role getModeratorRole() {
        return Config.getInstance().getGuild().getRoleById(moderator);
    }

    public Role getAdminRole() {
        return Config.getInstance().getGuild().getRoleById(admin);
    }

}