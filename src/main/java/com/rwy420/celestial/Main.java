package com.rwy420.celestial;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import com.rwy420.celestial.core.Architecture;
import com.rwy420.celestial.core.Format;
import com.rwy420.celestial.core.Lexer;
import com.rwy420.celestial.core.Linker;
import com.rwy420.celestial.core.Parser;
import com.rwy420.celestial.core.Token;
import com.rwy420.celestial.core.Variable;

public class Main
{
	public static Linker linker;

	public static void main(String[] args)
	{
		Variable variable = new Variable(12, 1234, 0, true, true, "abc");
		for(byte b : variable.getBytes())
		{
			System.out.println(String.format("0x%02X", b));
		}

		long beginTime = System.currentTimeMillis();

		linker = new Linker();
		linker.init(args);

		Lexer lexer = new Lexer();
		Parser parser = new Parser();

		long endTime = System.currentTimeMillis();

		System.out.println("Compiling took " + (endTime - beginTime)  + "ms.");
	}
}
