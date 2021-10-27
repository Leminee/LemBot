package tech.goodquestion.lembot.config;

import java.util.Map;

public class ReactionRoleMessage {

    private String channel;
    private String message;
    private Map<String, String> roles;

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getRoles() {
        return roles;
    }
}
