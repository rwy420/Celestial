package com.rwy420.celestial.core;

import java.util.ArrayList;
import java.util.List;

public class DataSection
{
	List<Integer> data;
	int pointer;

	public DataSection()
	{
		data = new ArrayList<>();
		pointer = 0;
	}

	public long addData(int[] value)
	{
		//pointer += value.length * 8;
		pointer += value.length;
		for(int b : value) data.add(b);
		//return pointer - value.length * 8;
		return pointer - value.length;
	}

	public long allocate(int sizeInBits)
	{
		int[] empty = new int[sizeInBits / 8];
		addData(empty);
		//return pointer - sizeInBits;
		return pointer - (sizeInBits / 8);
	}

	public void setRange(int[] data, int start)
	{
		for (int i = 0; i < data.length; i++)
		{
			int address = start + i;
			this.data.set(address, data[i]);
		}
	}

	public List<Integer> getRange(int start, int end)
	{
		return data.subList(start, end);
	}

	public List<Integer> getData()
	{
		return data;
	}
}
