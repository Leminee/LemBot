package tech.goodquestion.lembot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.command.KillSwitchCommand;
import tech.goodquestion.lembot.command.impl.BotDataCommand;
import tech.goodquestion.lembot.command.impl.CodeBlockHelpCommand;
import tech.goodquestion.lembot.command.impl.HelpListCommand;
import tech.goodquestion.lembot.command.impl.ServerRoleListCommand;
import tech.goodquestion.lembot.command.impl.database.*;
import tech.goodquestion.lembot.command.impl.moderation.*;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.config.ReactionRoleMessage;
import tech.goodquestion.lembot.database.*;
import tech.goodquestion.lembot.event.*;
import tech.goodquestion.lembot.library.ReactionManager;
import tech.goodquestion.lembot.music.command.PlayCommand;

import javax.security.auth.login.LoginException;

public class BotMain {

    public static JDA jda;
    public static final String PREFIX = "?";
    public static final String BOT_VERSION = "v3.1.2";

    public static void main(String[] args) {

        try {

            System.out.println("""                 
                     _      _____  __  __  ____    ___  _____\s
                    | |    | ____||  \\/  || __ )  / _ \\|_   _|
                    | |    |  _|  | |\\/| ||  _ \\ | | | | | | \s
                    | |___ | |___ | |  | || |_) || |_| | | | \s
                    |_____||_____||_|  |_||____/  \\___/  |_|\s
                    @Author: Lem                       """ + "                      " + BOT_VERSION);


            jda = JDABuilder
                    .createDefault(Config.getInstance().getToken())
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .build();


        } catch (LoginException loginException) {
            System.out.println(loginException.getMessage());
        }

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("?help"));

        initialiseObjects();
        setupReactionRoles();

    }

    private static void initialiseObjects(){

        CommandManager commandManager = new CommandManager(jda);

        commandManager.registerCommand(new HelpListCommand());
        commandManager.registerCommand(new ClearCommand());
        commandManager.registerCommand(new CodeBlockHelpCommand());
        commandManager.registerCommand(new BotDataCommand());
        commandManager.registerCommand(new WarnCommand());
        commandManager.registerCommand(new MuteCommand());
        commandManager.registerCommand(new BanCommand());
        commandManager.registerCommand(new UnmuteCommand());
        commandManager.registerCommand(new NextBumpTimeCommand());
        commandManager.registerCommand(new ServerRoleListCommand());
        commandManager.registerCommand(new ActiveUserRecordCommand());
        commandManager.registerCommand(new BumpCountCommand());
        commandManager.registerCommand(new JoiningDateCommand());
        commandManager.registerCommand(new MessageCountCommand());
        commandManager.registerCommand(new TopActiveChannelsCommand());
        commandManager.registerCommand(new TopBumperCommand());
        commandManager.registerCommand(new TopFlooderCommand());
        commandManager.registerCommand(new TopMonthlyBumperCommand());
        commandManager.registerCommand(new TopMonthlyFlooderCommand());
        commandManager.registerCommand(new ServerDataCommand());
        commandManager.registerCommand(new KillSwitchCommand());
        commandManager.registerCommand(new PlayCommand());
        commandManager.registerCommand(new TopBoosterCommand());

        BumpReminder bumpReminder = new BumpReminder();

        jda.addEventListener(bumpReminder);
        jda.addEventListener(new WelcomingMemberJoin());
        jda.addEventListener(new MemberJoinStorage());
        jda.addEventListener(new UserMessageCounter());
        jda.addEventListener(new BumpCounter());
        jda.addEventListener(new InviteLinkDeletion());
        jda.addEventListener(new UpdatedMessageStorage());
        jda.addEventListener(new DeletedMessageStorage());
        jda.addEventListener(new TopMonthlyNotifier());
        jda.addEventListener(new OnlineMemberStorage());
        jda.addEventListener(new ChannelMessageCounter());
        jda.addEventListener(new VoiceJoinedStorage());
        jda.addEventListener(new VoiceLeftStorage());
        jda.addEventListener(new LinkDeletion());
        jda.addEventListener(new VoiceMoved());
        jda.addEventListener(new MemberAuthorization());
        jda.addEventListener(new AmountMemberStatus());
        jda.addEventListener(new BumpReminderReactivation(bumpReminder));
        jda.addEventListener(new UpdatingUsername());
        jda.addEventListener(new MemberLeftStorage());
        jda.addEventListener(new AddingRole());
        jda.addEventListener(new SpamDetection());
        jda.addEventListener(new VoiceAutoCreation());
        jda.addEventListener(new AttachmentStorage());
        jda.addEventListener(new RoleEvents());
        jda.addEventListener(new InviteTracking());
        jda.addEventListener(new HappyNewYear());
        jda.addEventListener(new RaidDetection());
        jda.addEventListener(new MediaOnly());
    }


    private static void setupReactionRoles() {

        ReactionManager manager = new ReactionManager(jda);

        for (final ReactionRoleMessage reactionRoleMessage : Config.getInstance().getReactionRoles()) {
            reactionRoleMessage
                    .getRoles()
                    .forEach((emote, role) -> manager
                            .registerReaction(reactionRoleMessage
                                    .getChannel(), reactionRoleMessage
                                    .getMessage(), emote, role));

        }
    }
}
