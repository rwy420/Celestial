package com.rwy420.celestial.core;

import java.util.ArrayList;
import java.util.List;

public class Lexer
{
	public List<Token> tokenize(String line)
	{
		List<Token> result = new ArrayList<>();

		String current = "";
		boolean betweenQuotes = false;

		lineLoop:
		for(int i = 0; i < line.length(); i++)
		{
			char[] c = {line.charAt(i)};
			String s = new String(c);

			if(!betweenQuotes)
			{
				for(int j = 0; j < Tokens.values().length; j++)
				{
					if(s.equals(Tokens.values()[j].t))
					{
						if(s.equals("\""))
						{
							betweenQuotes = true;
							result.add(new Token(Tokens.TEXT, current));
							result.add(new Token(Tokens.QUOTES, "\""));
							current = "";
							continue lineLoop;
						}
						else if(s.equals("(") || s.equals(")") || s.equals("[") || s.equals("]") || s.equals(";") ||
								s.equals("*") || s.equals(","))
						{
							result.add(new Token(Tokens.TEXT, current));
							current = "";
						}
						result.add(new Token(Tokens.values()[j], s));
						current = "";
						continue lineLoop;
					}
				}
			
				if(s.equals(" ") && !betweenQuotes)
				{
					result.add(new Token(Tokens.TEXT, current));
					current = "";
					continue;
				}
			}
			if(s.equals("\""))
			{
				result.add(new Token(Tokens.TEXT, current));
				result.add(new Token(Tokens.QUOTES, "\""));
				current = "";
				betweenQuotes = false;
				continue lineLoop;
			}
			current = current + s;
		}

		for(int i = 0; i < result.size(); i++)
		{
			Token t = result.get(i);
			if(t.getToken() == Tokens.TEXT)
			{
				if(t.getValue().equals(""))
				{
					result.remove(i);
				}
			}
		}

		return result;
	}
}
