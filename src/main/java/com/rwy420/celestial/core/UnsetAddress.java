package com.rwy420.celestial.core;

import com.rwy420.celestial.Main;

/*
 * Types:
 * 1: [data section start] + address
 * 2: label
*/

public class UnsetAddress
{
	int pointer;
	int address;
	int type;

	public UnsetAddress(int pointer, int address, int type)
	{
		this.pointer = pointer;
		this.address = address;
		this.type = type;
	}

	public void fix()
	{
		switch(type)
		{
			case 1:
			{
				address += Main.linker.text.getEnd();

				int[] addressBytes = new int[4];

				StringBuilder hex = new StringBuilder(Integer.toHexString(address));

				int max = hex.length() + (hex.length() % 2);
				hex.setLength(max);

				int byteIndex = 0;
				for(int i = 0; i < max / 2; i++)
				{
					//String s = hex.substring(byteIndex, byteIndex += 2);
					addressBytes[(addressBytes.length - 1) - i] = Integer.valueOf(
							hex.substring(byteIndex, byteIndex += 2), 16);
				}

				Main.linker.text.setRange(addressBytes, pointer);
			}
			case 2:
			{
			}
		}
	}
}
