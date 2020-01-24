package me.moderator_man.pr.cmd;

import me.moderator_man.pr.AliasMap;
import me.moderator_man.pr.cmd.commands.FlushReports;
import me.moderator_man.pr.cmd.commands.Report;
import me.moderator_man.pr.cmd.commands.Reports;
import me.moderator_man.pr.cmd.commands.Resolve;

public class CommandManager
{
	private AliasMap<String, Command> commands;
	
	public CommandManager()
	{
		commands = new AliasMap<String, Command>();
	}
	
	public void onEnable()
	{
		// register commands
		register("report", new Report());
		
		// admin only
		register("resolve", new Resolve());
		register("reports", new Reports());
		register("flushreports", new FlushReports());
	}
	
	public void register(String realKey, Command command, String...aliases)
	{
		commands.put(realKey, command);
		for (String alias : aliases)
			commands.alias(realKey, alias);
	}
	
	public Command getCommand(String call)
	{
		return commands.get(call);
	}
}
