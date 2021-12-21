package tech.goodquestion.lembot.entity;

import net.dv8tion.jda.api.entities.Invite;

import java.util.Objects;

public class InviteData {

    private final long guildId;
    private int uses;

    public InviteData(final Invite invite) {
        this.guildId = Objects.requireNonNull(invite.getGuild()).getIdLong();
        this.uses = invite.getUses();
    }

    public long getGuildId() {
        return guildId;
    }

    public int getUses() {
        return uses;
    }

    public void incrementUses()
    {
        this.uses++;
    }
}

