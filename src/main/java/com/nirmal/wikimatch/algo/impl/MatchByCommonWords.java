package com.nirmal.wikimatch.algo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.nirmal.wikimatch.algo.api.IMatchingAlgorithm;
import com.nirmal.wikimatch.algo.params.MatchingControlParams;
import com.nirmal.wikimatch.common.Delimiters;
import com.nirmal.wikimatch.scoring.api.IScoringAlgorithm;

public class MatchByCommonWords implements IMatchingAlgorithm {
	private List<String> strArrQues;
	private String strAns;
	String strPara;
	
	private MatchingControlParams ctrlParams;
	private IScoringAlgorithm scoringAlgo;
	
	private HashMap<String, String> strArrProcessedQues;
	private String[] strArrProcessedAns;
	private HashMap<Integer, String> strArrProcessedPara;
	private ArrayList<Integer> matchAnswers = new ArrayList<Integer>();
	private HashMap<String, String> matchResults = new HashMap<String, String>();

	public MatchByCommonWords(MatchingControlParams param, IScoringAlgorithm scoreAlgo) {
		this.ctrlParams = param;
		this.scoringAlgo = scoreAlgo;
	}

	/*
	 * Assuming it makes sense only to let all the input parameters be changed at
	 * once
	 */
	public void setInputData(List<String> inpQuestions, String strAnswer, String strParagraph) {
		this.strArrQues = inpQuestions;
		this.strAns = strAnswer;
		this.strPara = strParagraph;
	}

	public void preProcessInput() {
		strArrProcessedQues = preProcessQuestions(strArrQues);
		strArrProcessedAns = preProcessAnswers(strAns);
		strArrProcessedPara = preProcessParagraph(strPara);
	}

	public HashMap<String, String> runAlgorithm() {
		for (String question : strArrProcessedQues.keySet()) {
			int maxScore = 0;
			int matchAnswer = 0;
			for (int j = 0; j < strArrProcessedPara.size(); j++) {
				String answer = strArrProcessedPara.get(j);
				int matchScore = scoringAlgo.getScore(strArrProcessedQues.get(question), answer);
				if (matchScore > maxScore) {
					maxScore = matchScore;
					matchAnswer = j;
				}
			}
			matchAnswers.add(matchAnswer);
			matchResults.put(question, strArrProcessedAns[matchAnswer]);
		}

		return matchResults;
	}

	private HashMap<String, String> preProcessQuestions(List<String> inpArrQues) {
		HashMap<String, String> processedArr = new HashMap<String, String>();

		for (String strQuestion : inpArrQues) {
			String ques = strQuestion;

			if (strQuestion.endsWith(Delimiters.QUESTION_MARK.getValue())) {
				ques = strQuestion.substring(0, strQuestion.length() - 1);
			}

			StringBuilder processedString = new StringBuilder();
			StringTokenizer tokenizer = new StringTokenizer(ques, ctrlParams.getWordSeparator().getValue());

			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken().toLowerCase();

				if (!ctrlParams.getQuestionKeyWords().contains(token) && !ctrlParams.getFillerWords().contains(token)) {
					processedString.append(token).append(ctrlParams.getWordSeparator().getValue());
				}
			}
			processedArr.put(strQuestion, processedString.toString().trim());
		}

		return processedArr;
	}

	private String[] preProcessAnswers(String strAns) {
		String[] strArrAns = strAns.toLowerCase().split(ctrlParams.getAnswerDelimiter().getValue());
		return strArrAns;
	}

	private HashMap<Integer, String> preProcessParagraph(String textPara) {
		HashMap<Integer, String> processedArr = new HashMap<Integer, String>();
		StringTokenizer paraTokenizer = new StringTokenizer(textPara.toLowerCase(),
				ctrlParams.getParagraphDelimiter().getValue());

		while (paraTokenizer.hasMoreElements()) {
			String paraLine = paraTokenizer.nextToken();
			int iPosition = 0;

			for (String processedAns : strArrProcessedAns) {
				if (paraLine.contains(processedAns)) {
					StringBuilder processedString = new StringBuilder();
					StringTokenizer tokenizer = new StringTokenizer(paraLine, ctrlParams.getWordSeparator().getValue());

					while (tokenizer.hasMoreTokens()) {
						String token = tokenizer.nextToken();

						if (!ctrlParams.getQuestionKeyWords().contains(token.toLowerCase())
								&& !ctrlParams.getFillerWords().contains(token.toLowerCase())) {
							processedString.append(token).append(ctrlParams.getWordSeparator().getValue());
						}
					}
					processedArr.put(iPosition, processedString.toString().trim());
					break;
				}
				iPosition++;
			}
		}

		return processedArr;
	}
}
