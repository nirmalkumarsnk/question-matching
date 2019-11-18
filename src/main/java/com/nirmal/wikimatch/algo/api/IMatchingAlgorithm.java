package com.nirmal.wikimatch.algo.api;

import java.util.HashMap;
import java.util.List;

public interface IMatchingAlgorithm {
	public void setInputData(List<String> inpQuestions, String strAnswer, String strParagraph);
	
	public void preProcessInput();
	
	public HashMap<String,String> runAlgorithm();
}
