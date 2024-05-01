package com.rwy420.celestial.core;

import java.util.UUID;

public class SourceFile
{
	private String path, name, uid, linkerName;

	public SourceFile(String path, String name)
	{
		this.path = path;
		this.name = name;
		this.uid = UUID.randomUUID().toString().split("-")[0];
		this.linkerName = name + uid;
	}

	public String getPath()
	{
		return path;
	}

	public String getName()
	{
		return name;
	}

	public String getUid()
	{
		return uid;
	}

	public String getLinkerName()
	{
		return linkerName;
	}
}
