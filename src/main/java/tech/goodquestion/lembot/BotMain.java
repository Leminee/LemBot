package tech.goodquestion.lembot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goodquestion.lembot.command.impl.BotsCommand;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.command.impl.KillSwitchCommand;
import tech.goodquestion.lembot.command.impl.ParserCommand;
import tech.goodquestion.lembot.command.impl.*;
import tech.goodquestion.lembot.command.impl.database.*;
import tech.goodquestion.lembot.command.impl.moderation.*;
import tech.goodquestion.lembot.config.CommandRole;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.config.ReactionRoleMessage;
import tech.goodquestion.lembot.database.*;
import tech.goodquestion.lembot.event.*;
import tech.goodquestion.lembot.library.ReactionManager;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BotMain {

    public static JDA jda;

    public static void main(String[] args) {

        try {

            Logger logger = LoggerFactory.getLogger(BotMain.class);
            logger.info("{} {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm")), Config.getInstance().getBotConfig().getVersion());

            System.out.println("""
                     \033[0;34m\040\040\040\040\040\040\040\040\040\040\040\040
                     _      _____  __  __  ____    ___  _____\s
                    | |    | ____||  \\/  || __ )  / _ \\|_   _|
                    | |    |  _|  | |\\/| ||  _ \\ | | | | | | \s
                    | |___ | |___ | |  | || |_) || |_| | | | \s
                    |_____||_____||_|  |_||____/  \\___/  |_|\s
                    @Author: Lem\040\040\040\040\040\040\040\040\040\040\040\040\040\040\040\040\040\040\040\040\040\040"""
                    + Config.getInstance().getBotConfig().getVersion() + "\033[0m\n\n"
            );

            jda = JDABuilder
                    .createDefault(Config.getInstance().getBotConfig().getToken())
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .enableCache(CacheFlag.ONLINE_STATUS)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .build();

        } catch (LoginException loginException) {
            System.out.println(loginException.getMessage());
        }

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("?help"));

        initializeObjects();
        setupReactionRoles();
    }

    private static void initializeObjects() {

        CommandManager commandManager = new CommandManager(jda);

        commandManager.registerCommand(new ActiveUserRecordCommand());
        commandManager.registerCommand(new AdvertisingCommand());
        commandManager.registerCommand(new BanCommand());
        commandManager.registerCommand(new BotDataCommand());
        commandManager.registerCommand(new BumpCountCommand());
        commandManager.registerCommand(new ClearCommand());
        commandManager.registerCommand(new CodeBlockHelpCommand());
        commandManager.registerCommand(new HelpListCommand());
        commandManager.registerCommand(new LastJoinDateCommand());
        commandManager.registerCommand(new KillSwitchCommand());
        commandManager.registerCommand(new MessageCountCommand());
        commandManager.registerCommand(new MuteCommand());
        commandManager.registerCommand(new NextBumpTimeCommand());
        commandManager.registerCommand(new PasswordCheckCommand());
        commandManager.registerCommand(new ServerDataCommand());
        commandManager.registerCommand(new ServerRoleListCommand());
        commandManager.registerCommand(new TopActiveChannelsCommand());
        commandManager.registerCommand(new TopBumperCommand());
        commandManager.registerCommand(new TopFlooderCommand());
        commandManager.registerCommand(new TopMonthlyBumperCommand());
        commandManager.registerCommand(new TopMonthlyFlooderCommand());
        commandManager.registerCommand(new UnmuteCommand());
        commandManager.registerCommand(new MemberInfoCommand());
        commandManager.registerCommand(new MemberLogsCommand());
        commandManager.registerCommand(new WarnCommand());
        commandManager.registerCommand(new ParserCommand());
        commandManager.registerCommand(new BotsCommand());
        commandManager.registerCommand(new UnbanCommand());
        commandManager.registerCommand(new UnwarnCommand());
        commandManager.registerCommand(new UserSanctionHistory());

        BumpReminder bumpReminder = new BumpReminder();

        jda.addEventListener(bumpReminder);
        jda.addEventListener(new AddingRole());
        jda.addEventListener(new AmountMemberStatus());
        jda.addEventListener(new AttachmentStorage());
        jda.addEventListener(new BumpCounter());
        jda.addEventListener(new BumpReminderReactivation(bumpReminder));
        jda.addEventListener(new ChannelMessageCounter());
        jda.addEventListener(new DeletedMessageStorage());
        jda.addEventListener(new HappyNewYear());
        jda.addEventListener(new InviteLinkDeletion());
        jda.addEventListener(new InviteTracking());
        jda.addEventListener(new LinkDeletion());
        jda.addEventListener(new MemesOnlyChannel());
        jda.addEventListener(new MemberAuthorization());
        jda.addEventListener(new MemberJoinStorage());
        jda.addEventListener(new MemberLeftStorage());
        jda.addEventListener(new OnlineMemberStorage());
        jda.addEventListener(new RaidDetection());
        jda.addEventListener(new RoleEvents());
        jda.addEventListener(new SpamDetection());
        jda.addEventListener(new UpdatedMessageStorage());
        jda.addEventListener(new ProfileUpdating());
        jda.addEventListener(new UserMessageCounter());
        jda.addEventListener(new VoiceAutoCreation());
        jda.addEventListener(new VoiceJoinedStorage());
        jda.addEventListener(new VoiceLeftStorage());
        jda.addEventListener(new VoiceMovedStorage());
        jda.addEventListener(new WelcomingMemberJoin());
        jda.addEventListener(new HoppingDetection());

        setupRoleCommands(commandManager);
    }

    private static void setupReactionRoles() {

        ReactionManager manager = new ReactionManager(jda);

        for (final ReactionRoleMessage reactionRoleMessage : Config.getInstance().getReactionRoles()) {
            reactionRoleMessage
                    .getRoles()
                    .forEach((emote, role) -> manager.registerReaction(reactionRoleMessage.getChannel(), reactionRoleMessage.getMessage(), emote, role));
        }
    }

    private static void setupRoleCommands(CommandManager commandManager) {
        for (CommandRole role : Config.getInstance().getCommandRoles()) {
            RoleToggleCommand.register(commandManager, role.getAbbr(), role.getRole());
        }
    }
}
