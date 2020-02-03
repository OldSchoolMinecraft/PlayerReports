package me.moderator_man.pr;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerHandler extends PlayerListener
{
	PlayerReports pr = PlayerReports.instance;
	
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		Player ply = event.getPlayer();
		
		if (ply.isOp() || ply.hasPermission("pr.admin"))
		{
			ArrayList<SystemReport> reports = pr.reportManager.getReports();
			int unresolvedReports = 0;
			if (reports.size() > 0)
				for (SystemReport report : reports)
					if (!report.resolved)
						unresolvedReports++;
			if (unresolvedReports == 1)
				sendDelayedMessage(ply, String.format("%s%s%s%s%s%s", ChatColor.RED, "There is currently ", ChatColor.YELLOW, unresolvedReports, ChatColor.RED, " unresolved report."));
			else
				sendDelayedMessage(ply, String.format("%s%s%s%s%s%s", ChatColor.RED, "There are currently ", ChatColor.YELLOW, unresolvedReports, ChatColor.RED, " unresolved reports."));
			if (reports.size() >= 9900)
			{
				sendDelayedMessage(ply, "You have less than 100 reports left before the system is full!");
				sendDelayedMessage(ply, "If you don't need them, clear all resolved reports with /flushreports.");
			}
		}
	}
	
	private void sendDelayedMessage(CommandSender sender, String msg)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(pr, new Runnable()
		{
			public void run()
			{
				if (sender != null)
					sender.sendMessage(msg);
			}
		}, 1);
	}
}
