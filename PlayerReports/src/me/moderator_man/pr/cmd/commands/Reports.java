package me.moderator_man.pr.cmd.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.moderator_man.pr.SystemReport;
import me.moderator_man.pr.cmd.Command;

public class Reports extends Command
{
	public Reports()
	{
		super("Reports", "List all reports.", true);
	}

	public boolean run(CommandSender sender, String[] args)
	{
		if (!sender.hasPermission("pr.view"))
		{
			sender.sendMessage(ChatColor.RED + "You don't have permission to view reports!");
			return true;
		}
		
		ArrayList<SystemReport> reports = pr.reportManager.getReports();
		
		if (reports.size() < 1)
		{
			pr.sendError(sender, "There are no reports!");
			return true;
		}
		
		boolean showResolved = false;
		if (args.length > 0)
		{
			for (String arg : args)
				if (arg.equalsIgnoreCase("resolved"))
					showResolved = true;
		}
		
		Queue<String> messages = new LinkedList<String>();
		int unresolvedReports = 0;
		
		if (showResolved)
			messages.add("All reports (include resolved):");
		else
			messages.add("All reports:");
		
		for (SystemReport report : reports)
		{
			if (report.resolved && !showResolved)
				continue;
			unresolvedReports++;
			if (showResolved)
			{
				messages.add(String.format("%s[%s%s%s] %s[%s%s%s] %s%s %s-> %s%s%s: %s%s",
						ChatColor.GRAY, ChatColor.AQUA, report.getID(), ChatColor.GRAY,
						ChatColor.GRAY, report.resolved ? ChatColor.GREEN : ChatColor.RED, report.resolved ? "RESOLVED" : "UNRESOLVED", ChatColor.GRAY,
						ChatColor.GREEN, report.accuser,
						ChatColor.GRAY,
						ChatColor.RED, report.accused, ChatColor.GRAY,
						ChatColor.AQUA, report.message));
			} else {
				messages.add(String.format("%s[%s%s%s] %s%s %s-> %s%s%s: %s%s",
						ChatColor.GRAY, ChatColor.AQUA, report.getID(), ChatColor.GRAY,
						ChatColor.GREEN, report.accuser,
						ChatColor.GRAY,
						ChatColor.RED, report.accused, ChatColor.GRAY,
						ChatColor.AQUA, report.message));
			}
		}
		
		if (unresolvedReports < 1)
		{
			pr.sendError(sender, "There are no unresolved reports!");
			return true;
		} else {
			while (!messages.isEmpty())
				sender.sendMessage(messages.remove());
		}
		
		return true;
	}
}
