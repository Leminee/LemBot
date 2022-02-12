package tech.goodquestion.lembot.config;

import net.dv8tion.jda.api.entities.Role;

public class RoleConfig {

    private long bump;
    private long mute;
    private long warn;
    private long coding;
    private long hacking;
    private long moderator;

    public long getBumpRoleId() {
        return bump;
    }

    public Role getCodingRole() {
        return Config.getInstance().getGuild().getRoleById(coding);
    }

    public Role getHackingRole() {
        return Config.getInstance().getGuild().getRoleById(hacking);
    }

    public Role getMuteRole() {
        return Config.getInstance().getGuild().getRoleById(mute);
    }

    public Role getWarnRole() {
        return Config.getInstance().getGuild().getRoleById(warn);
    }

    public Role getModeratorRole(){
        return Config.getInstance().getGuild().getRoleById(moderator);
    }


}
