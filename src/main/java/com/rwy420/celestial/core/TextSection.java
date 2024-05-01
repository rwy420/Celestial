package com.rwy420.celestial.core;

import java.util.ArrayList;
import java.util.List;

public class TextSection
{
	List<Integer> data;
	long pointer;

	public TextSection()
	{
		this.data = new ArrayList<>();
	}

	public void add(int b)
	{
		data.add(b);
		//pointer += 8;
		pointer++;
	}

	public void setRange(int[] data, int start)
	{
		for (int i = 0; i < data.length; i++)
		{
			int address = start + i;
			this.data.set(address, data[i]);
		}
	}

	public List<Integer> getData()
	{
		return data;
	}

	public int getEnd()
	{
		return data.size();
	}

	public long getPointer()
	{
		return pointer;
	}
}
