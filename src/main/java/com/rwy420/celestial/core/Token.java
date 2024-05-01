package com.rwy420.celestial.core;

public class Token
{
	private Tokens token;
	private String value;

	public Token(Tokens token, String value)
	{
		this.token = token;
		this.value = value;
	}

	public Tokens getToken() 
	{
		return token;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
