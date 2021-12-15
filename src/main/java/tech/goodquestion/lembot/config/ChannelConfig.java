package tech.goodquestion.lembot.config;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class ChannelConfig {

    private long sanction;
    private long newArrivals;
    private long bump;
    private long voice;
    private long yourProjects;
    private long memberCount;
    private long selfRoles;
    private long staff;


    public TextChannel getSanctionChannel() {
        return Config.getInstance().getGuild().getTextChannelById(sanction);
    }

    public TextChannel getNewArrivalsChannel() {
        return Config.getInstance().getGuild().getTextChannelById(newArrivals);
    }

    public TextChannel getBumpChannel() {
        return Config.getInstance().getGuild().getTextChannelById(bump);
    }

    public TextChannel getVoiceChatChannel() {
        return Config.getInstance().getGuild().getTextChannelById(voice);
    }

    public TextChannel getYourProjectsChannel() {
        return Config.getInstance().getGuild().getTextChannelById(yourProjects);
    }

        public VoiceChannel getMemberCountChannel() {
        return Config.getInstance().getGuild().getVoiceChannelById(memberCount);
    }

    public TextChannel getSelfRolesChannel() {
        return Config.getInstance().getGuild().getTextChannelById(selfRoles);
    }

    public TextChannel getStaffRoomChannel() {
        return Config.getInstance().getGuild().getTextChannelById(staff);
    }

}
