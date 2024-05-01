package com.rwy420.celestial.core;

import java.util.ArrayList;
import java.util.List;

import com.rwy420.celestial.Main;

/*
global void printf(char[] string)
{
	
}

global uint32 offset = 12;

global void kernelMain()
{
	uint32 number = 0;
	uint32 pointer = &number;

	number = 12;

	printf("Hello, World");
}
*/

public class Parser
{
	public List<Variable> variables;
	public List<Type> types;

	public Parser()
	{
		variables = new ArrayList<>();
		types = new ArrayList<>();

		types.add(new Type("int8", 8, true));
		types.add(new Type("uint8", 8, false));
		types.add(new Type("int16", 16, true));
		types.add(new Type("uint16", 16, false));
		types.add(new Type("int32", 32, true));
		types.add(new Type("uint32", 32, false));
		types.add(new Type("void", 0, false));
		types.add(new Type("char", 8, false));
	}

	public void parse(List<Token> tokens)
	{
		if(tokens.size() == 0) return;

		// define function
		if(tokens.get(tokens.size() - 1).getToken() == Tokens.OPEN_CURLY_BRACKET)
		{
			String name = "";
			for(Type t : types)
			{
				if(tokens.get(0).getValue().replaceAll("	", "").equals(t.getName()))
				{
					name = tokens.get(1).getValue();
					long address = Main.linker.text.getPointer();

					if(name.equals("_main"))
					{
						System.out.println("ENTRY: " + Long.toHexString(address));
						int[] start = splitIntoBytes(address, 4);
						Main.linker.text.setRange(start, 1);
					}

					System.out.println("Type: " + t.getName());
					System.out.println("Name: " + name);
					System.out.println("Address: " + address);

					// push fp
					Main.linker.text.add(0x73);
					Main.linker.text.add(0xF);

					// mov sp, fp
					Main.linker.text.add(0x0E);
					Main.linker.text.add(0xE);
					Main.linker.text.add(0xF);

					if(name.equals("_main"))
					{
						Main.linker.text.add(0x71);
						Main.linker.text.add(0x11);
						Main.linker.text.add(0x11);

						Main.linker.text.add(0x5E);
						Main.linker.text.add(0x00);
						Main.linker.text.add(0x00);
						Main.linker.text.add(0x00);
						Main.linker.text.add(0x05);
					}

					List<Token> arguments = tokens.subList(3, tokens.size() - 2);

					int registerOffset = 13;

					for(int i = 0; i < arguments.size(); i += 0)
					{
						String typeName = arguments.get(i).getValue();
						i++;
						boolean pointer = false;
						pointer = arguments.get(i).getValue().equals("*");
						if(pointer) i++;
						String argName = arguments.get(i).getValue();

						for(Type argType : types)
						{
							if(argType.getName().equals(typeName))
							{
								// get arguments from stack and create variables in stack
								if(argType.getSize() == 8)
								{

								}
								else if(argType.getSize() == 16)
								{
									Main.linker.text.add(0x75);
									Main.linker.text.add(registerOffset);
								}
								else if(argType.getSize() == 32)
								{
									Main.linker.text.add(0x76);
									Main.linker.text.add(registerOffset);
								}

								registerOffset--;
							}
						}

						if(arguments.size() - 1 <= i)
						{
							break;
						}
						else
						{
							i += 2;
							continue;
						}
					}
				}
			}

			// mov fp, sp
			Main.linker.text.add(0x0E);
			Main.linker.text.add(0x0F);
			Main.linker.text.add(0x0E);

			// pop fp
			Main.linker.text.add(0x76);
			Main.linker.text.add(0x0F);

			if(!(name.equals("_main")))
			{
				//ret
				Main.linker.text.add(0x60);
			}
			else
			{
				Main.linker.text.add(0xFF);
			}

			return;
		}

		// create variable
		for(Type t : types)
		{
			if(tokens.get(0).getValue().replaceAll("	", "").equals(t.getName()))
			{
				String name = tokens.get(1).getValue().replaceAll(" ", "").replaceAll("	", "");
				int value = getValue(tokens.subList(3, tokens.size()));
				long address = Main.linker.data.allocate(t.getSize());
				Variable variable = new Variable(t.getSize(), value, address, t.isSigned(), false, name);

				if(variable.getSize() == 8)
				{
					Main.linker.text.add(0x19);
				}
				else if(variable.getSize() == 16)
				{
					Main.linker.text.add(0x1B);
				}
				else if(variable.getSize() == 32)
				{
					Main.linker.text.add(0x1C);
				}

				int[] bytes = splitIntoBytes(variable.getValue(), variable.getSize() / 8);

				for(int i : bytes)
				{
					Main.linker.text.add(i);
				}

					//Main.linker.unsetAddresses.add(new UnsetAddress((int) Main.linker.text.getPointer() / 8,
					//			(int) variable.getAddress(), 1));
					//
				Main.linker.unsetAddresses.add(new UnsetAddress((int) Main.linker.text.getPointer(),
							(int) variable.getAddress(), 1));

				for(int i = 0; i < 4; i++)
				{
					Main.linker.text.add(0x00);
				}

				variables.add(variable);
				
				return;
			}
		}

		// set variable
		for(Variable variable : variables)
		{
			if(variable.getName().equals(tokens.get(0).getValue().replace("	", "").replace(" ", "")))
			{
				if(variable.getSize() == 8)
				{
					Main.linker.text.add(0x19);
				}
				else if(variable.getSize() == 16)
				{
					Main.linker.text.add(0x1B);
				}
				else if(variable.getSize() == 32)
				{
					Main.linker.text.add(0x1C);
				}

				variable.setValue(getValue(tokens.subList(2, tokens.size())));

				int[] bytes = splitIntoBytes(variable.getValue(), variable.getSize() / 8);

				for(int i : bytes)
				{
					Main.linker.text.add(i);
				}

					//Main.linker.unsetAddresses.add(new UnsetAddress((int) Main.linker.text.getPointer() / 8,
					//			(int) variable.getAddress(), 1));
					//
				
				Main.linker.unsetAddresses.add(new UnsetAddress((int) Main.linker.text.getPointer(),
						(int) variable.getAddress(), 1));
				for(int i = 0; i < 4; i++)
				{
					Main.linker.text.add(0x00);
				}

				variables.add(variable);
				
				return;
			}
		}
	}

	public int getValue(List<Token> tokens)
	{
		if(tokens.size() == 2 && isNumber(tokens.get(0).getValue()))
		{
			return Integer.valueOf(tokens.get(0).getValue());
		}

		for(int i = 0; i < tokens.size(); i++)
		{
			Token t = tokens.get(i);

			for(Variable v : variables)
			{
				if(t.getValue().equals(v.getName()))
				{
					if(tokens.get(i - 1).getToken() == Tokens.ADDRESS)
					{
						t.setValue(String.valueOf(v.getAddress()));
						tokens.set(i, t);
						//System.out.println("\n" + v.getValue() + "\n");
						if(tokens.size() == 3 && isNumber(tokens.get(1).getValue()))
						{
							//System.out.println(Main.linker.text.getPointer());
							//System.out.println(v.getAddress());
							Main.linker.unsetAddresses.add(new UnsetAddress((int) Main.linker.text.getPointer() + 1
										, (int) v.getAddress(), 1));
							return Integer.valueOf(tokens.get(1).getValue());
						}
					}

					t.setValue(String.valueOf(v.getValue()));
					tokens.set(i, t);
					//System.out.println("\n" + v.getValue() + "\n");
					if(tokens.size() == 2 && isNumber(tokens.get(0).getValue()))
					{
						return Integer.valueOf(tokens.get(0).getValue());
					}
				}
			}
		}

		tokens.remove(tokens.size() - 1);

		StringBuilder expr = new StringBuilder();

		for(Token t : tokens)
		{
			if(!(isNumber(t.getValue()) || isOperator(t)))
			{
				System.out.println("Illegal maths operation '" + t.getValue() + "'");
				System.exit(-1);
			}
			expr.append(t.getValue());
		}

		int result = (int) eval(expr.toString());
		return result;
	}


	private boolean isNumber(String s)
	{
		try
		{
			Double.parseDouble(s);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}

	private boolean isOperator(Token t)
	{
		if(t.getToken() == Tokens.OP_PLUS || t.getToken() == Tokens.OP_DIVIDE ||
				t.getToken() == Tokens.OP_MINUS || t.getToken() == Tokens.STAR) return true;

		return false;
	}

	private int[] splitIntoBytes(long num, int bytes)
	{
		int[] result = new int[bytes];
		StringBuilder hex = new StringBuilder();
		String val = Long.toHexString(num);

		for(int i = 0; i < bytes * 2 - val.length(); i++)
		{
			hex.append("0");
		}
		hex.append(val);

		int byteIndex = 0;
		for(int i = 0; i < bytes; i++)
		{
			result[i] = Integer.parseInt(hex.substring(byteIndex, byteIndex += 2), 16);
		}

		return result;
	}

	//https://stackoverflow.com/a/26227947

	public double eval(final String str) {
		return new Object() {
			int pos = -1, ch;
        
	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }
        
	        boolean eat(int charToEat) {
	         while (ch == ' ') nextChar();
	         if (ch == charToEat) {
	             nextChar();
	             return true;
	         }
	         return false;
	     }
        
	     double parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);	            
				return x;
        }
                
	     double parseExpression() {
	         double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
			}
        
		    double parseTerm() {
		        double x = parseFactor();
		        for (;;) {
		            if      (eat('*')) x *= parseFactor(); // multiplication
				    else if (eat('/')) x /= parseFactor(); // division
			        else return x;
		        }
	        }
        
		    double parseFactor() {
	            if (eat('+')) return +parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus
	            
	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
			        x = parseExpression();
		            if (!eat(')')) throw new RuntimeException("Missing ')'");
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
              while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	             x = Double.parseDouble(str.substring(startPos, this.pos));
	         } else if (ch >= 'a' && ch <= 'z') { // functions
	             while (ch >= 'a' && ch <= 'z') nextChar();
	                String func = str.substring(startPos, this.pos);
	                if (eat('(')) {
	                    x = parseExpression();
	                    if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
					} else {
					    x = parseFactor();
				    }
			        if (func.equals("sqrt")) x = Math.sqrt(x);
		            else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
					else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
				    else throw new RuntimeException("Unknown function: " + func);
			   } else {
			      throw new RuntimeException("Unexpected: " + (char)ch);
			   }
            
			    if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
            
		     return x;
		  }
		}.parse();
	}
}
