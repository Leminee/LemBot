package tech.goodquestion.lembot.config;

import net.dv8tion.jda.api.entities.Role;

public final class RoleConfig {

    private long bump;
    private long coding;
    private long moderator;
    private long admin;

    public long getBumpRoleId() {
        return bump;
    }

    public Role getModeratorRole() {
        return Config.getInstance().getGuild().getRoleById(moderator);
    }

    public Role getAdminRole() {
        return Config.getInstance().getGuild().getRoleById(admin);
    }

}