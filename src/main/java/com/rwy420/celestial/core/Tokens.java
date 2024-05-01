package com.rwy420.celestial.core;

public enum Tokens
{
	OP_PLUS("+"),
	OP_MINUS("-"),
	OP_DIVIDE("/"),

	STAR("*"),

	ADDRESS("&"),

	OPEN_BRACKET("("),
	CLOSE_BRACKET(")"),
	OPEN_SQUARE_BRACKET("["),
	CLOSE_SQUARE_BRACKET("]"),
	OPEN_CURLY_BRACKET("{"),
	CLOSE_CURLY_BRACKET("}"),

	EQUALS("="),
	EXCLAMATION_MARK("!"),

	TEXT(""),
	NUMBER(""),

	QUOTES("\""),
	SINGLE_QUOTE("'"),
	SEMICOLON(";"),
	COMMA(",");

	public String t;

	private Tokens(String t)
	{
		this.t = t;
	}
}
