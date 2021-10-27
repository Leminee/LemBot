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

        CommandManager commands = new CommandManager(jda);
        commands.registerCommand(new HelpList());
        commands.registerCommand(new ClearCommand());
        commands.registerCommand(new HelloCommand());
        commands.registerCommand(new PingCommand());
        commands.registerCommand(new CodeBlockHelpCommand());
        commands.registerCommand(new MetaQuestionCommand());
        commands.registerCommand(new BotSourceCommand());
        commands.registerCommand(new WarnCommand());
        commands.registerCommand(new MuteCommand());
        commands.registerCommand(new KickCommand());
        commands.registerCommand(new BanCommand());
        commands.registerCommand(new UnmuteCommand());
        commands.registerCommand(new NextBumpTimeCommand());
        commands.registerCommand(new PasswordCheckCommand());
        commands.registerCommand(new ServerRoleListCommand());
        commands.registerCommand(new UserRoleListCommand());

        commands.registerCommand(new ActiveUserRecordCommand());
        commands.registerCommand(new BumpCountCommand());
        commands.registerCommand(new MessageCountCommand());
        commands.registerCommand(new TopActiveChannelsCommand());
        commands.registerCommand(new TopBumperCommand());
        commands.registerCommand(new TopMessageCommand());
        commands.registerCommand(new TopPingedUserCommand());
        commands.registerCommand(new TopEmojiCommand());

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
        setupRoleCommands(commands);
    }

    private static void setupRoleCommands(CommandManager commands) {
        for (CommandRole role : Config.getInstance().getCommandRoles()) {
            RoleToggleCommand.register(commands, role.getAbbr(), role.getRole());
        }
    }

    private static void setupReactionRoles() {

        ReactionManager manager = new ReactionManager(jda);

        for (ReactionRoleMessage reactionRoleMessage : Config.getInstance().getReactionRoles()) {
            reactionRoleMessage.getRoles().forEach((emote, role) -> manager.registerReaction(reactionRoleMessage.getChannel(), reactionRoleMessage.getMessage(), emote, role));

        }
    }
}
