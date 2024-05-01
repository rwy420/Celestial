package com.rwy420.celestial.core;

public class Function
{
	String name;
	long address;

	public Function(String name, long address)
	{
		this.name = name;
		this.address = address;
	}

	public String getName()
	{
		return name;
	}

	public long getAddress()
	{
		return address;
	}
}
