package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.lib.Helper;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tech.goodquestion.lembot.lib.Helper.PREFIX;

public class CommandManager extends ListenerAdapter {

    public static final Pattern COMMAND_PATTERN = Pattern.compile("[" + PREFIX + "]([^ ]+)(.*)");
    private static CommandManager INSTANCE = null;

    public static CommandManager getInstance() {
        return INSTANCE;
    }

    private final Map<String, BotCommand> commands = new HashMap<>();
    private final JDA jda;
    private final Set<String> helpLists = new HashSet<>();

    public void registerCommand(BotCommand cmd) {
        commands.put(cmd.getName(), cmd);

        if (cmd.getHelpList() != null) {
            helpLists.add(cmd.getHelpList());
        }
    }

    public BotCommand getCommand(String name) {
        return commands.getOrDefault(name, null);
    }

    public CommandManager(JDA jda) {
        this.jda = jda;
        INSTANCE = this;
        jda.addEventListener(this);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final Message msg = event.getMessage();

        // ignore bot users
        if (msg.getAuthor().isBot()) {
            return;
        }

        Matcher commandMatcher = COMMAND_PATTERN.matcher(msg.getContentRaw());

        // message is not a command
        if (!commandMatcher.matches()) {
            return;
        }

        String command = commandMatcher.group(1);

        // no command with that name
        if (!commands.containsKey(command)) {
            return;
        }

        // split arguments
        String[] args = commandMatcher.group(2).split(" ");

        // remove space from argument array
        args = Arrays.copyOfRange(args, 1, args.length);

        BotCommand executor = commands.get(command);

        if (!executor.isPermitted(msg.getMember())) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Permission Denied";
            Helper.createEmbed(embedError, "", embedDescription, Color.RED);
            msg.getChannel().sendMessage(embedError.build()).queue();
            return;
        }

        executor.dispatch(msg, event.getChannel(), event.getMember(), args);
    }

    public Map<String, BotCommand> getCommands() {
        return commands;
    }

    public JDA getJDA() {
        return jda;
    }

    public Set<String> getHelpLists() {
        return helpLists;
    }

}
