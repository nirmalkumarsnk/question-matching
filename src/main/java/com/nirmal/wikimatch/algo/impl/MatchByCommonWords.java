package com.nirmal.wikimatch.algo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.nirmal.wikimatch.algo.api.IMatchingAlgorithm;
import com.nirmal.wikimatch.algo.params.MatchingControlParams;
import com.nirmal.wikimatch.common.Delimiters;
import com.nirmal.wikimatch.common.StringProcessingUtils;
import com.nirmal.wikimatch.scoring.api.IScoringAlgorithm;

public class MatchByCommonWords implements IMatchingAlgorithm {
	private MatchingControlParams ctrlParams;
	private IScoringAlgorithm scoringAlgo;

	private String strAns;
	private String strPara;
	private List<String> strArrQues;

	private List<String> noiseStrings;

	private List<String> arrProcessedAns;
	private HashMap<String, String> mapProcessedQues;
	private HashMap<String, String> mapProcessedPara;
	private HashMap<String, String> matchResults;

	public MatchByCommonWords(MatchingControlParams param, IScoringAlgorithm scoreAlgo) {
		this.ctrlParams = param;
		this.scoringAlgo = scoreAlgo;

		noiseStrings = new ArrayList<String>();
		noiseStrings.addAll(ctrlParams.questionKeyWords());
		noiseStrings.addAll(ctrlParams.fillerWords());
	}

	public String getAnswersToCheck() {
		return strAns;
	}

	public String getParaToSearch() {
		return strPara;
	}

	public List<String> getQuestionsToAnswer() {
		return new ArrayList<String>(strArrQues);
	}

	public List<String> getNoiseStrings() {
		return new ArrayList<String>(noiseStrings);
	}

	public List<String> getProcessedAnswers() {
		return arrProcessedAns;
	}

	public HashMap<String, String> getProcessedQuestions() {
		return new HashMap<String, String>(mapProcessedQues);
	}

	public HashMap<String, String> getProcessedPara() {
		return new HashMap<String, String>(mapProcessedPara);
	}

	/******************************************************************
	 * Assuming it makes sense only to let all the input parameters be changed at
	 * once
	 *****************************************************************/
	public void setInputData(List<String> inpQuestions, String strAnswer, String strParagraph) {
		this.strArrQues = new ArrayList<String>(inpQuestions);
		this.strAns = strAnswer;
		this.strPara = strParagraph;
	}

	public void preProcessInput() {
		/******************************************************************
		 * Pre-process questions by removing question mark and question key-words like
		 * what, why, when, etc.
		 *****************************************************************/
		mapProcessedQues = new HashMap<String, String>();
		for (String strQuestion : strArrQues) {
			String ques = strQuestion;

			if (strQuestion.endsWith(Delimiters.QUESTION_MARK.getValue())) {
				ques = strQuestion.substring(0, strQuestion.length() - 1);
			}

			String cleanString = StringProcessingUtils.tokenizeAndCleanseString(ques,
					ctrlParams.wordSeparator().getValue(), noiseStrings);

			mapProcessedQues.put(strQuestion, cleanString);
		}

		/******************************************************************
		 * Pre-process answers by splitting them individually
		 ******************************************************************/
		arrProcessedAns = Arrays.asList(strAns.toLowerCase().split(ctrlParams.answerDelimiter().getValue()));

		/******************************************************************
		 * Pre-process paragraph by finding the whole sentence using the answer phrases
		 * given. Also removes question and filler words that are not considered for the
		 * matching
		 ******************************************************************/
		StringTokenizer paraTokenizer = new StringTokenizer(strPara, ctrlParams.paragraphDelimiter().getValue());

		mapProcessedPara = new HashMap<String, String>();
		HashMap<String, String> mapAnsToParaLine = new HashMap<String, String>();

		while (paraTokenizer.hasMoreElements()) {
			String paraLine = paraTokenizer.nextToken().trim();
			for (String processedAns : arrProcessedAns) {				
				if (paraLine.toLowerCase().contains(processedAns.trim())) {					
					String existingAns = mapAnsToParaLine.get(paraLine);

					if (null == existingAns || existingAns.length() < processedAns.length()) {
						String cleanString = StringProcessingUtils.tokenizeAndCleanseString(paraLine,
								ctrlParams.wordSeparator().getValue(), noiseStrings);
						mapProcessedPara.put(paraLine, cleanString);
						mapAnsToParaLine.put(paraLine, processedAns);
						break;
					}
				}
			}
		}		
	}

	/******************************************************************
	 * Matches all input questions with all lines in the paragraph that contain the
	 * answers. Picks the one with the highest score as the match
	 ***************************************************************/
	public void runAlgorithm() {
		matchResults = new HashMap<String, String>();

		for (String question : mapProcessedQues.keySet()) {
			int maxScore = 0;
			String matchAnswer = "";
			for (String answer : mapProcessedPara.keySet()) {
				int matchScore = scoringAlgo.getScore(mapProcessedQues.get(question), mapProcessedPara.get(answer));
				if (matchScore > maxScore) {
					maxScore = matchScore;
					matchAnswer = answer;
				}
			}
			matchResults.put(question, matchAnswer);
		}
	}

	public HashMap<String, String> getResult() {
		return new HashMap<String, String>(matchResults);
	}
}
