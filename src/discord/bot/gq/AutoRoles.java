package discord.bot.gq;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class AutoRoles {


    public void ac(String[] args, MessageReceivedEvent event) {
        String roleName = "";
        for(String arg : args) {
            roleName += arg + " ";
        }
        roleName = roleName.substring(0, roleName.length() -1);
        List<Role> roleList = event.getGuild().getRoles();
        for(Role role : roleList) {

            }
        }

    }

