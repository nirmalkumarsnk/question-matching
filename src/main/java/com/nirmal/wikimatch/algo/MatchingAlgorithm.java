package com.nirmal.wikimatch.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class MatchingAlgorithm {
	private ControlParameters ctrlParams;
	
	private List<String> strArrQues;
	private String strAns;
	private String strPara;
	
	private ArrayList<String> strArrProcessedQues;
	private String[] strArrProcessedAns;
	private HashMap<Integer, String> strArrProcessedPara;
	private ArrayList<Integer> matchAnswers = new ArrayList<Integer>();
	private HashMap<String, String> matchResults = new HashMap<String, String>();
	
	public void setControlParams(ControlParameters params) {
		this.ctrlParams=params;
	}
	
	public void setInputData(List<String> inpQuestions, String strAnswer, String strParagraph)
	{
		this.strArrQues = inpQuestions;
		this.strAns = strAnswer;
		this.strPara = strParagraph;
	}
	
	public abstract void preProcessInput();
	
	public abstract HashMap<String,String> runAlgorithm();
}
