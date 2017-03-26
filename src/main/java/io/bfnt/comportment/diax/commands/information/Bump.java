package io.bfnt.comportment.diax.commands.information;

import io.bfnt.comportment.diax.api.command.CommandDescription;
import io.bfnt.comportment.diax.api.command.DiaxCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;

/**
 * Created by Comporment on 25/03/2017 at 17:46
 * Dev'ving like a sir since 1998. | https://github.com/Comportment
 */
@CommandDescription(name = "bump", guildOnly = true, emoji = "🎉")
public class Bump extends DiaxCommand
{
    private static HashMap<Guild, Long> bumps = new HashMap<>();
    public void execute(Message trigger)
    {
        int coolDown = 14400;
        if (bumps.containsKey(trigger.getGuild()))
        {
            long timeSinceLast = (trigger.getCreationTime().toEpochSecond() - bumps.get(trigger.getGuild()));
            if (timeSinceLast >= coolDown)
            {
                bump(trigger);
            }
            else
            {
                trigger.getChannel().sendMessage(makeMessage("Error", "Please wait " + (coolDown - timeSinceLast) + "s to bump this server again.").build()).queue();
            }
        }
        else
        {
            bump(trigger);
        }
    }
    private void bump(Message trigger)
    {
        Guild guild = trigger.getGuild();
        String invite = "https://discord.gg/" + trigger.getGuild().getPublicChannel().createInvite().complete().getCode();
        bumps.remove(guild);
        trigger.getJDA().getGuildById("293889712014360586").getTextChannelById("294519934996971520").sendMessage(new EmbedBuilder().setAuthor(guild.getName(), invite, guild.getIconUrl()).setDescription(String.format("Join %s by clicking this [link](%s).", guild.getName(), invite)).setColor(trigger.getGuild().getMember(trigger.getAuthor()).getColor()).build()).queue();
        trigger.getChannel().sendMessage(makeMessage("Bumped!", trigger.getGuild().getName() + " has been bumped!").build()).queue();
        bumps.put(guild, trigger.getCreationTime().toEpochSecond());
    }
}