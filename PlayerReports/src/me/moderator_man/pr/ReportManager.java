package me.moderator_man.pr;

import java.io.File;
import java.util.ArrayList;

import me.moderator_man.pr.serial.FormatReader;
import me.moderator_man.pr.serial.FormatWriter;

public class ReportManager
{
	private ArrayList<SystemReport> reports;
	
	public ReportManager()
	{
		reports = new ArrayList<SystemReport>();
	}
	
	public void loadExistingReports()
	{
		try
		{
			File dir = new File("pr-data/reports");
			FormatReader<SystemReport> reader = new FormatReader<SystemReport>();
			for (File file : dir.listFiles())
			{
				add(reader.read(file.getAbsolutePath()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Something went wrong while loading reports: " + ex.getMessage());
		}
	}
	
	public void saveAllReports()
	{
		try
		{
			FormatWriter<SystemReport> writer = new FormatWriter<SystemReport>();
			for (SystemReport report : reports)
			{
				String pod = String.format("pr-data/reports/%s.report", report.getID());
				File file = new File(pod);
				if (file.exists())
					file.delete();
				writer.write(report, file.getAbsolutePath());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Something went wrong while saving reports: " + ex.getMessage());
		}
	}
	
	public void add(SystemReport report)
	{
		reports.add(report);
	}
	
	public void remove(SystemReport report)
	{
		reports.remove(report);
	}
	
	public void remove(int index)
	{
		reports.remove(index);
	}
	
	public void resolve(int id)
	{
		for (SystemReport report : reports)
			if (report.getID() == id)
				report.resolve();
	}
	
	public void reopen(int id)
	{
		for (SystemReport report : reports)
			if (report.getID() == id)
				report.reopen();
	}
	
	public boolean hasReport(int id)
	{
		for (SystemReport report : reports)
			if (report.getID() == id)
				return true;
		return false;
	}
	
	public ArrayList<SystemReport> getReports()
	{
		return reports;
	}
}
