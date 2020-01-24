package me.moderator_man.pr.cmd.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.moderator_man.pr.cmd.Command;

public class Resolve extends Command
{
	public Resolve()
	{
		super("Resolve", "Mark a report as resolved.", true);
	}

	public boolean run(CommandSender sender, String[] args)
	{
		if (args.length < 1)
			return false;
		
		try
		{
			int id = Integer.parseInt(args[0]);
			if (!pr.reportManager.hasReport(id))
			{
				pr.sendError(sender, "There are no reports with that ID.");
				return true;
			}
			pr.reportManager.resolve(id);
			pr.sendSuccess(sender, "Marked report as resolved!");
			
			for (Player ply : pr.getServer().getOnlinePlayers())
			{
				if (ply.isOp() || ply.hasPermission("pr.admin"))
				{
					ply.sendMessage(String.format("%sPlayer report %s#%s%s %shas been resolved!", ChatColor.GREEN, ChatColor.GRAY, ChatColor.AQUA, id, ChatColor.GREEN));
				}
			}
			return true;
		} catch (Exception ex) {
			pr.sendError(sender, "Must be a number!");
		}
		
		return false;
	}
}
