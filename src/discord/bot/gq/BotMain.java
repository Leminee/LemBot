package discord.bot.gq;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import javax.security.auth.login.LoginException;

public class BotMain {

    public static JDA jda;
    public static String prefix = "?";
    public static final String token = "ODE1ODk0ODA1ODk2ODg4MzYy.YDzDQA.9cGPspX9uvUevnBtniGjkKYJD9I";


    public static void main(String[] args) {

        try {
            jda = JDABuilder.createDefault(token).build();

        } catch (LoginException e) {
            e.printStackTrace();
        }

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("With Commands"));

        jda.addEventListener(new Clear());
        jda.addEventListener(new Reply());
        jda.addEventListener(new RemindMe());


    }
}