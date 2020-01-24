package me.moderator_man.pr;

import java.io.File;
import java.io.Serializable;

public class SystemReport implements Serializable
{
	private static final long serialVersionUID = 1L;

	private int id;

	public String accuser;
	public String accused;
	public String message;
	public boolean resolved;

	public SystemReport(String accuser, String accused, String message)
	{
		this.id = generateUniqueID();

		this.accuser = accuser;
		this.accused = accused;
		this.message = message;
		this.resolved = false;
	}

	public int getID()
	{
		return id;
	}

	public void resolve()
	{
		resolved = true;
	}

	public void reopen()
	{
		resolved = false;
	}

	private int generateUniqueID()
	{
		int id = PlayerReports.instance.random.nextInt(9999);
		while (new File(String.format("pr-data/reports/%s.report", id)).exists())
		{
			id = PlayerReports.instance.random.nextInt(9999);
		}
		return id;
	}
}
