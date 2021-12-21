package tech.goodquestion.lembot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goodquestion.lembot.lib.EmbedColorHelper;
import tech.goodquestion.lembot.lib.Helper;

import java.io.IOException;
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

    private final Map<String, IBotCommand> commands = new HashMap<>();
    private final JDA jda;
    private final Set<String> helpLists = new HashSet<>();

    public void registerCommand(IBotCommand command) {
        commands.put(command.getName(), command);

        if (command.getHelpList() != null) {
            helpLists.add(command.getHelpList());
        }
    }

    public IBotCommand getCommand(String name) {
        return commands.getOrDefault(name, null);
    }

    public CommandManager(JDA jda) {
        this.jda = jda;
        INSTANCE = this;
        jda.addEventListener(this);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        final Message message = event.getMessage();

        if (message.getAuthor().isBot()) {
            return;
        }

        Matcher commandMatcher = COMMAND_PATTERN.matcher(message.getContentRaw());

        if (!commandMatcher.matches()) {
            return;
        }

        String command = commandMatcher.group(1).toLowerCase();

        if (!commands.containsKey(command)) {
            return;
        }

        String[] args = commandMatcher.group(2).split(" ");

        args = Arrays.copyOfRange(args, 1, args.length);

        IBotCommand executor = commands.get(command);

        if (!executor.isPermitted(message.getMember())) {
            EmbedBuilder embedError = new EmbedBuilder();
            String embedDescription = "Permission Denied";
            Helper.createEmbed(embedError, "", embedDescription, EmbedColorHelper.ERROR);
            message.getChannel().sendMessage(embedError.build()).queue();
            return;
        }

        try {
            executor.dispatch(message, event.getChannel(), event.getMember(), args);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Map<String, IBotCommand> getCommands() {
        return commands;
    }

    public JDA getJDA() {
        return jda;
    }

    public Set<String> getHelpLists() {
        return helpLists;
    }

}
