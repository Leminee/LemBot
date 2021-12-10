package tech.goodquestion.lembot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
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
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_PRESENCES)
                    .build();

        } catch (LoginException loginException) {
            loginException.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("?help"));

        CommandManager command = new CommandManager(jda);

        command.registerCommand(new HelpList());
        command.registerCommand(new ClearCommand());
        command.registerCommand(new HelloCommand());
        command.registerCommand(new PingCommand());
        command.registerCommand(new CodeBlockHelpCommand());
        command.registerCommand(new MetaQuestionCommand());
        command.registerCommand(new BotSourceCommand());
        command.registerCommand(new WarnCommand());
        command.registerCommand(new MuteCommand());
        command.registerCommand(new BanCommand());
        command.registerCommand(new UnmuteCommand());
        command.registerCommand(new NextBumpTimeCommand());
        command.registerCommand(new PasswordCheckCommand());
        command.registerCommand(new ServerRoleListCommand());

        command.registerCommand(new ActiveUserRecordCommand());
        command.registerCommand(new BumpCountCommand());
        command.registerCommand(new JoiningDateCommand());
        command.registerCommand(new MessageCountCommand());
        command.registerCommand(new TopActiveChannelsCommand());
        command.registerCommand(new TopBumperCommand());
        command.registerCommand(new TopMessageCommand());
        command.registerCommand(new TopEmojiCommand());
        command.registerCommand(new TopMonthlyBumperCommand());
        command.registerCommand(new TopMonthlyFlooderCommand());


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
        jda.addEventListener(new AutoAnswering());
        jda.addEventListener(new AddingRole());
        jda.addEventListener(new SpamDetection());
        jda.addEventListener(new VoiceCreation());
        jda.addEventListener(new AttachmentStorage());

        setupReactionRoles();
        setupRoleCommands(command);
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
