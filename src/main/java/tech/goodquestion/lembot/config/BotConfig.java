package tech.goodquestion.lembot.config;

public class BotConfig {

    private String token;
    private char prefix;
    private String name;
    private String version;
    private long id;

    public String getToken() {
        return token;
    }

    public char getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public long getId(){
        return id;
    }
}
