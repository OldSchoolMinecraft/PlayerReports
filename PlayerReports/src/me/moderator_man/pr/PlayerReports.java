package me.moderator_man.pr;

import java.io.File;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

import me.moderator_man.pr.cmd.CommandManager;
import me.moderator_man.pr.config.ConfigurationManager;

public class PlayerReports extends JavaPlugin
{
	public static PlayerReports instance;
	
	public CommandManager cmdm;
	public ReportManager reportManager;
	public ConfigurationManager config;
	public Random random;
	
	public void onEnable()
	{
		instance = this;
		
		File dir1 = new File("pr-data");
		if (!dir1.exists())
			dir1.mkdir();
		else if (!dir1.isDirectory()) {
			dir1.delete();
			dir1.mkdir();
		}
		
		File dir2 = new File("pr-data/reports");
		if (!dir2.exists())
			dir2.mkdir();
		else if (!dir2.isDirectory()) {
			dir2.delete();
			dir2.mkdir();
		}
		
		cmdm = new CommandManager();
		reportManager = new ReportManager();
		config = new ConfigurationManager();
		random = new Random();
		cmdm.onEnable();
		config.load();
		
		reportManager.loadExistingReports();
		
		getServer().getPluginManager().registerEvent(Type.PLAYER_LOGIN, new PlayerHandler(), Priority.Normal, this);
		
		System.out.println("PlayerReports enabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		String command = cmd.getName().toLowerCase();
		me.moderator_man.pr.cmd.Command cmnd = cmdm.getCommand(command);
		
		if (cmnd != null)
		{
			if (cmnd.isPlayerOnly() && !(sender instanceof Player))
			{
				sendError(sender, "Only players can use this command!");
				return true;
			}
			
			if (cmnd.isAdminOnly() && (!sender.isOp() || !sender.hasPermission("pr.admin")))
			{
				sendError(sender, "Insufficient permissions!");
				return true;
			}
			
			return cmnd.run(sender, args);
		}
		
		return false;
	}
	
	public void sendError(CommandSender sender, String msg)
	{
		sender.sendMessage(String.format("%s%s", ChatColor.RED, msg));
	}
	
	public void sendSuccess(CommandSender sender, String msg)
	{
		sender.sendMessage(String.format("%s%s", ChatColor.GREEN, msg));
	}
	
	public void onDisable()
	{
		reportManager.saveAllReports();
		
		System.out.println("PlayerReports disabled.");
	}
}
