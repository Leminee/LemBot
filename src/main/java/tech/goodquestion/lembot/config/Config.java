package tech.goodquestion.lembot.config;

import net.dv8tion.jda.api.entities.Guild;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import tech.goodquestion.lembot.BotMain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public final class Config {

    public static final String CONFIG_FILE = "lembot.config.yml";
    private static Config INSTANCE = null;
    private List<ReactionRoleMessage> reactionRoles;
    private List<CommandRole> commandRoles;
    private DatabaseConfig database;
    private RoleConfig role;
    private ChannelConfig channel;
    private CategoryConfig category;
    private BotConfig bot;
    private long guild;
    private String serverName;

    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = Config.load(CONFIG_FILE);
        }
        return INSTANCE;
    }

    public static Config load(String filePath) {
        File configFile = new File(filePath);
        Yaml configParser = new Yaml(new Constructor(Config.class, null));
        configParser.setBeanAccess(BeanAccess.FIELD);
        try {
            return configParser.load(new FileInputStream(configFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("config file could not be opened");
        }
    }

    public List<ReactionRoleMessage> getReactionRoles() {
        return reactionRoles;
    }

    public List<CommandRole> getCommandRoles() {
        return commandRoles;
    }

    public DatabaseConfig getDatabaseConfig() {
        return database;
    }


    public RoleConfig getRoleConfig() {
        return role;
    }

    public ChannelConfig getChannelConfig() {
        return channel;
    }

    public CategoryConfig getCategoryConfig() {
        return category;
    }

    public BotConfig getBotConfig() {
        return bot;
    }

    public Guild getGuild() {
        return BotMain.jda.getGuildById(guild);
    }

    public String getServerName() {
        return serverName;
    }
}