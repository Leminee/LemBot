package tech.goodquestion.lembot.config;

import net.dv8tion.jda.api.entities.Category;

public final class CategoryConfig {
    private long voice;

    public Category getVoiceCategory() {
        return Config.getInstance().getGuild().getCategoryById(voice);
    }
}
