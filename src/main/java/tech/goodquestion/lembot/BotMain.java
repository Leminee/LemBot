package tech.goodquestion.lembot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.command.impl.*;
import tech.goodquestion.lembot.command.impl.db.*;
import tech.goodquestion.lembot.command.impl.moderation.*;
import tech.goodquestion.lembot.config.Config;
import tech.goodquestion.lembot.config.ReactionRoleMessage;
import tech.goodquestion.lembot.database.*;
import tech.goodquestion.lembot.event.*;
import tech.goodquestion.lembot.lib.ReactionManager;

import javax.security.auth.login.LoginException;

public class BotMain {

    public static JDA jda;

    public static void main(String[] args) {
        try {

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

        CommandManager commandManager = new CommandManager(jda);

        commandManager.registerCommand(new HelpList());
        commandManager.registerCommand(new ClearCommand());
        commandManager.registerCommand(new CodeBlockHelpCommand());
        commandManager.registerCommand(new BotDataCommand());
        commandManager.registerCommand(new WarnCommand());
        commandManager.registerCommand(new MuteCommand());
        commandManager.registerCommand(new BanCommand());
        commandManager.registerCommand(new UnmuteCommand());
        commandManager.registerCommand(new NextBumpTimeCommand());
        commandManager.registerCommand(new PasswordCheckCommand());
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
        commandManager.registerCommand(new ServerData());

        jda.addEventListener(new Reminder());
        jda.addEventListener(new WelcomingMemberJoin());
        jda.addEventListener(new MemberJoinStorage());
        jda.addEventListener(new UserMessageCounter());
        jda.addEventListener(new BumpCounter());
        jda.addEventListener(new InviteLinkDeletion());
        jda.addEventListener(new UpdatedMessageStorage());
        jda.addEventListener(new DeletedMessageStorage());
        jda.addEventListener(new TopMonthlyNotifier());
        jda.addEventListener(new ActiveUsers());
        jda.addEventListener(new ChannelMessageCounter());
        jda.addEventListener(new VoiceJoinedStorage());
        jda.addEventListener(new VoiceLeftStorage());
        jda.addEventListener(new LinkDeletion());
        jda.addEventListener(new VoiceMoved());
        jda.addEventListener(new UserAuthorization());
        jda.addEventListener(new AmountMemberStatus());
        jda.addEventListener(new ReminderReactivation());
        jda.addEventListener(new UpdatingUsername());
        jda.addEventListener(new MemberLeftStorage());
        jda.addEventListener(new AddingRole());
        jda.addEventListener(new SpamDetection());
        jda.addEventListener(new VoiceAutoCreation());
        jda.addEventListener(new AttachmentStorage());
        jda.addEventListener(new RoleEvents());
        jda.addEventListener(new InviteTracking());
        jda.addEventListener(new HappyNewYear());

        setupReactionRoles();
    }


    private static void setupReactionRoles() {

        ReactionManager manager = new ReactionManager(jda);

        for (ReactionRoleMessage reactionRoleMessage : Config.getInstance().getReactionRoles()) {
            reactionRoleMessage.getRoles().forEach((emote, role) -> manager.registerReaction(reactionRoleMessage.getChannel(), reactionRoleMessage.getMessage(), emote, role));

        }
    }
}
