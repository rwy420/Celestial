package com.rwy420.celestial.core;

import java.nio.ByteBuffer;

public class Variable
{
	private int size;
	private long value, address;
	private boolean signed, global;
	private String name;

	public Variable(int size, long value, long address, boolean signed, boolean global, String name)
	{
		this.size = size;
		this.value = value;
		this.address = address;
		this.signed = signed;
		this.global = global;
		this.name = name;

		adjustSize();
	}

	public Variable(int size, long address, boolean signed, boolean global, String name)
	{
		this.size = size;
		this.address = address;
		this.signed = signed;
		this.global = global;
		this.name = name;

		adjustSize();
	}

	private void adjustSize()
	{
		if((size % 8) != 0)
		{
			size += (size % 8);
		}

		//System.out.println(size);
	}

	public byte[] getBytes()
	{
		ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
		byteBuffer.putLong(value);
		byte[] temp = byteBuffer.array();
		byte[] result = new byte[size / 8];
		for(int i = 0; i < result.length; i++)
		{
			result[result.length - i - 1] = temp[temp.length - i - 1];
		}

		return result;
	}

	public long getValue() 
	{
		return value;
	}

	public void setValue(long value) 
	{
		this.value = value;
	}

	public int getSize() 
	{
		return size;
	}

	public String getName() 
	{
		return name;
	}

	public boolean isSigned()
	{
		return signed;
	}

	public boolean isGlobal()
	{
		return global;
	}

	public long getAddress()
	{
		return address;
	}
}
