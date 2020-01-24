package me.moderator_man.pr.cmd.commands;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import me.moderator_man.pr.SystemReport;
import me.moderator_man.pr.cmd.Command;

public class FlushReports extends Command
{
	public FlushReports()
	{
		super("FlushReports", "Remove all resolved reports.", true);
	}

	public boolean run(CommandSender sender, String[] args)
	{
		ArrayList<SystemReport> reports = pr.reportManager.getReports();
		
		int index = 0;
		for (SystemReport report : reports)
		{
			if (!report.resolved)
				continue;
			try
			{
				String path = String.format("pr-data/reports/%s.report", report.getID());
				File file = new File(path);
				file.delete();
				pr.reportManager.remove(index);
				index++;
			} catch (Exception ex) {
				ex.printStackTrace();
				pr.sendError(sender, "An unknown error occurred!");
			}
		}
		
		pr.sendSuccess(sender, "Successfully flushed all resolved reports!");
		
		return true;
	}
}
