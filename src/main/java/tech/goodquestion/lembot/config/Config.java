package tech.goodquestion.lembot.config;

import net.dv8tion.jda.api.entities.Guild;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import tech.goodquestion.lembot.BotMain;

import java.io.*;
import java.util.List;

public class Config {

    public static final String CONFIG_FILE = "example.config.yml";
    private static Config INSTANCE = null;
    private String token;
    private List<ReactionRoleMessage> reactionRoles;
    private List<CommandRole> commandRoles;
    private DatabaseConfig database;
    private long guild;
    private RoleConfig role;
    private ChannelConfig channel;
    private UserConfig user;
    private CategoryConfig category;

    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = Config.load(CONFIG_FILE);
        }
        return INSTANCE;
    }

    public static Config load(String filePath) {
        File configFile = new File(filePath);
        Yaml configParser = new Yaml(new Constructor(Config.class));
        configParser.setBeanAccess(BeanAccess.FIELD);
        try {
            return configParser.load(new FileInputStream(configFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("config file could not be opened");
        }
    }


    public void save() {
        Yaml configDumper = new Yaml();
        configDumper.setBeanAccess(BeanAccess.FIELD);
        try {
            FileWriter configWriter = new FileWriter(CONFIG_FILE);
            configDumper.dump(this, configWriter);
            configWriter.close();
        } catch (IOException e) {
            System.err.println("Error writing config file");
        }
    }


    public String getToken() {
        return token;
    }

    public List<ReactionRoleMessage> getReactionRoles() {
        return reactionRoles;
    }

    public List<CommandRole> getCommandRoles() {
        return commandRoles;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }

    public Guild getGuild() {
        return BotMain.jda.getGuildById(guild);
    }

    public RoleConfig getRole() {
        return role;
    }

    public ChannelConfig getChannel() {
        return channel;
    }

    public CategoryConfig getCategory() {
        return category;
    }

    public UserConfig getUser() {
        return user;
    }

}
