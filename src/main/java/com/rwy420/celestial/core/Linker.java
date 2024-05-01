package com.rwy420.celestial.core;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.rwy420.celestial.Main;

public class Linker
{
	List<String> cesFiles;
	String outFile, arch, format;

	Lexer lexer;
	Parser parser;

	public List<UnsetAddress> unsetAddresses;

	public TextSection text;
	public DataSection data;

	public Linker()
	{
		cesFiles = new ArrayList<>();
		text = new TextSection();
		unsetAddresses = new ArrayList<>();
		data = new DataSection();
		lexer = new Lexer();
		parser = new Parser();
	}

	public void init(String[] args)
	{
		File linkerFile = new File("./linkerfile");
		if(!linkerFile.exists())
		{
			System.out.println("Could not find linkerfile!");
			System.exit(-1);
		}
		try
		{
			Scanner linkerFileScanner = new Scanner(linkerFile);
		
			while(linkerFileScanner.hasNextLine())
			{
				String line = linkerFileScanner.nextLine();

				if(line.startsWith("out: "))
				{
					outFile = line.replaceAll("out: ", "");
				}
				else if(line.startsWith("format: "))
				{
					format = line.replaceAll("format: ", "");
				}
				else if(line.startsWith("arch: "))
				{
					arch = line.replaceAll("arch: ", "");
				}
				else if(line.startsWith("src: "))
				{
					String source = line.replaceAll("src: ", "");
					if(!(new File(source).exists()))
					{
						System.out.println("Source file '" + source + "' does not exist!");
						System.exit(-1);
					} 
					if(!(source.endsWith(".ces")))
					{
						System.out.println("Source file '" + source + "' is not a .ces file!");
						System.exit(-1);
					}

					cesFiles.add(source);
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		Main.linker.text.add(0x4F);
		Main.linker.text.add(0x00);
		Main.linker.text.add(0x00);
		Main.linker.text.add(0x00);
		Main.linker.text.add(0x10);

		try
		{
			for(String ces : cesFiles)
			{
				File cesFile = new File(ces);
				Scanner cesScanner = new Scanner(cesFile);
				while(cesScanner.hasNextLine())
				{
					parser.parse(lexer.tokenize(cesScanner.nextLine()));
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		adjustAddresses();

		List<Integer> result = new ArrayList<>();
		result.addAll(text.getData());
		result.addAll(data.getData());

		try
		{
			File bin = new File(outFile);
			bin.createNewFile();
			RandomAccessFile rafBin = new RandomAccessFile(bin, "rw");
			rafBin.setLength(result.size());
			for(int b : result)
			{
				rafBin.write(b);
				System.out.print("0x" + Integer.toHexString(b) + ",");
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("Successfully written " + result.size() + " Bytes.");
	}

	public void adjustAddresses()
	{
		for(UnsetAddress address : unsetAddresses)
		{
			address.fix();
		}
	}
}
