package tech.goodquestion.lembot.config;

import net.dv8tion.jda.api.entities.Role;

public class RolesConfig {

    private long bump;
    private long mute;
    private long warn;

    public long getBumpRoleId() {
        return bump;
    }

    public Role getBumpRole() {
        return Config.getInstance().getGuild().getRoleById(bump);
    }

    public Role getMuteRole() {
        return Config.getInstance().getGuild().getRoleById(mute);
    }

    public Role getWarnRole() {
        return Config.getInstance().getGuild().getRoleById(warn);
    }

}
