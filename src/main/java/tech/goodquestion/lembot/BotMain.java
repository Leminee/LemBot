package tech.goodquestion.lembot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import tech.goodquestion.lembot.command.AutoAnswering;
import tech.goodquestion.lembot.command.CommandManager;
import tech.goodquestion.lembot.command.impl.*;
import tech.goodquestion.lembot.command.impl.db.*;
import tech.goodquestion.lembot.command.impl.moderation.*;
import tech.goodquestion.lembot.config.CommandRole;
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
                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .build();
        } catch (LoginException loginException) {
            loginException.printStackTrace();
        }

        System.out.println("Bot logged in as " + jda.getSelfUser().getName() + "!");

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("?help"));

        CommandManager cmds = new CommandManager(jda);
        cmds.registerCommand(new HelpList());
        cmds.registerCommand(new ClearCommand());
        cmds.registerCommand(new HelloCommand());
        cmds.registerCommand(new PingCommand());
        cmds.registerCommand(new CodeBlockHelpCommand());
        cmds.registerCommand(new MetaQuestionCommand());
        cmds.registerCommand(new BotSourceCommand());
        cmds.registerCommand(new WarnCommand());
        cmds.registerCommand(new MuteCommand());
        cmds.registerCommand(new KickCommand());
        cmds.registerCommand(new BanCommand());
        cmds.registerCommand(new UnmuteCommand());
        cmds.registerCommand(new NextBumpTimeCommand());
        cmds.registerCommand(new PasswordCheckCommand());
        cmds.registerCommand(new ServerRoleListCommand());
        cmds.registerCommand(new UserRoleListCommand());

        cmds.registerCommand(new ActiveUserRecordCommand());
        cmds.registerCommand(new BumpCountCommand());
        cmds.registerCommand(new MessageCountCommand());
        cmds.registerCommand(new TopActiveChannelsCommand());
        cmds.registerCommand(new TopBumperCommand());
        cmds.registerCommand(new TopMessageCommand());
        cmds.registerCommand(new TopPingedUserCommand());
        cmds.registerCommand(new TopEmojiCommand());

        jda.addEventListener(new Reminder());
        jda.addEventListener(new WelcomingMemberJoin());
        jda.addEventListener(new MemberJoinStorage());
        jda.addEventListener(new UserMessageCounter());
        jda.addEventListener(new BumpCounter());
        jda.addEventListener(new InviteLinkDeletion());
        jda.addEventListener(new UpdatedMessageStorage());
        jda.addEventListener(new DeletedMessageStorage());
        jda.addEventListener(new TopListReminder());
        jda.addEventListener(new ActiveUsers());
        jda.addEventListener(new ChannelMessageCounter());
        jda.addEventListener(new VoiceJoinedStorage());
        jda.addEventListener(new VoiceLeftStorage());
        jda.addEventListener(new LinkDeletion());
        jda.addEventListener(new VoiceMoved());
        jda.addEventListener(new UserAuthorization());
        jda.addEventListener(new AmountMemberStatus());
        jda.addEventListener(new ReminderReactivation());

        jda.addEventListener(new MemberLeftStorage());
        jda.addEventListener(new AutoAnswering());

        setupReactionRoles();
        setupRoleCommands(cmds);
    }

    private static void setupRoleCommands(CommandManager cmds) {
        for (CommandRole role : Config.getInstance().getCommandRoles()) {
            RoleToggleCommand.register(cmds, role.getAbbr(), role.getRole());
        }
    }

    private static void setupReactionRoles() {
        ReactionManager manager = new ReactionManager(jda);
        for (ReactionRoleMessage msg : Config.getInstance().getReactionRoles()) {
            msg.getRoles().forEach((emote, role) -> manager.registerReaction(msg.getChannel(), msg.getMessage(), emote, role));
        }
    }
}
