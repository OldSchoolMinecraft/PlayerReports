package me.moderator_man.pr.cmd.commands;

import java.awt.Color;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.moderator_man.pr.DiscordWebhook;
import me.moderator_man.pr.SystemReport;
import me.moderator_man.pr.cmd.Command;

public class Report extends Command
{
	public Report()
	{
		super("Report", "Report a player to the staff.");
	}

	public boolean run(CommandSender sender, String[] args)
	{
		if (args.length < 2)
			return false;
		
		String accuser;
		String accused = args[0];
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < args.length; i++)
			sb.append(args[i] + " ");
		String message = sb.toString().trim();
		
		if (sender instanceof Player)
		{
			Player ply = (Player) sender;
			accuser = ply.getName();
		} else {
			accuser = "CONSOLE";
		}
		
		try
		{
			SystemReport report = new SystemReport(accuser, accused, message);
			pr.reportManager.add(report);
			
			String url = pr.config.getString("webhook", "changeme");
			if (url != "changeme")
			{
				DiscordWebhook webhook = new DiscordWebhook(url);
			    webhook.setContent("@here");
				webhook.addEmbed(new DiscordWebhook.EmbedObject()
			    		.setTitle("Player Report")
			            .setColor(Color.RED)
			            .addField("Accuser", accuser, false)
			            .addField("Accused", accused, false)
			            .addField("Message", message, false));
			    webhook.execute();
			    
			    for (Player op : pr.getServer().getOnlinePlayers())
			    {
			    	if (op.isOp() || op.hasPermission("pr.admin"))
			    	{
			    		op.sendMessage(String.format("%s[%s%s%s] %s%s %s-> %s%s%s: %s%s",
								ChatColor.GRAY, ChatColor.AQUA, report.getID(), ChatColor.GRAY,
								ChatColor.GREEN, report.accuser,
								ChatColor.GRAY,
								ChatColor.RED, report.accused, ChatColor.GRAY,
								ChatColor.AQUA, report.message));
			    	}
			    }
			} else {
				pr.sendError(sender, "Please edit the PlayerReports config!");
			}
		} catch (Exception ex) {
			System.out.println("Failed to send report to Discord: " + ex.getMessage());
		}
		
		pr.sendSuccess(sender, "Your report has been submitted. Thank you!");
		
		System.out.println(String.format("%s submitted a report.", ((Player)sender).getName()));
		
		return true;
	}
}
