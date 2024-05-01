package com.rwy420.celestial.core;

public class Architecture
{
	String name;
	Format[] formats;

	public Architecture(String name, Format[] formats)
	{
		this.name = name;
		this.formats = formats;
	}

	public String getName()
	{
		return name;
	}

	public Format[] getFormats()
	{
		return formats;
	}
}
