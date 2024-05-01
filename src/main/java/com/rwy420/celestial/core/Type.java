package com.rwy420.celestial.core;

public class Type
{
	String name;
	int size;
	boolean signed;

	public Type(String name, int size, boolean signed)
	{
		this.name = name;
		this.size = size;
		this.signed = signed;
	}

	public String getName()
	{
		return name;
	}

	public int getSize() 
	{
		return size;
	}

	public boolean isSigned()
	{
		return signed;
	}
}
