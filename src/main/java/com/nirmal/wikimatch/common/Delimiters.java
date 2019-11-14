package com.nirmal.wikimatch.common;

public enum Delimiters {
	SPACE(" "),
	FULL_STOP("."),
	SEMI_COLON(";"),
	QUESTION_MARK("?");
	
	private String strDelim;
	
	public String getDelimiter()
	{
		return this.strDelim;
	}
	
	private Delimiters(String strInput)
	{
		this.strDelim = strInput;
	}
}
