package com.nirmal.wikimatch.algo.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.nirmal.wikimatch.algo.params.MatchingControlParams;
import com.nirmal.wikimatch.common.Delimiters;
import com.nirmal.wikimatch.exceptions.ValidationException;
import com.nirmal.wikimatch.scoring.api.IScoringAlgorithm;
import com.nirmal.wikimatch.scoring.impl.SimpleStringMatchScoring;
import com.nirmal.wikimatch.scoring.params.ScoringControlParams;

public class MatchByCommonWordsTest {
	private MatchingControlParams matchCtrlParams;
	private ScoringControlParams scoreCtrlParams;
	private IScoringAlgorithm scoringAlgo;

	ArrayList<String> questionKeyWords = new ArrayList<String>(Arrays.asList("why", "who", "what", "which", "when"));
	ArrayList<String> fillerWords = new ArrayList<String>(Arrays.asList("a", "an", "the", "then", "is", "it", "of",
			"are", "to", "some", "their", "by", "and", "this", "that", "in", "but", "such", "as"));

	ArrayList<String> strArrQues = new ArrayList<String>();
	String strAns = "is answer1 in full; Is answer2 in; is complete answer3";
	String para = "This is answer1 in full. This is answer2 in full sentence. This is complete answer3";

	@Before
	public void setUp() throws ValidationException {
		matchCtrlParams = new MatchingControlParams.Builder().setAnswerDelimiter(Delimiters.SEMI_COLON)
				.setParagraphDelimiter(Delimiters.FULL_STOP).setFillerWordsToFilter(fillerWords)
				.setQuestionWordsToFilter(questionKeyWords).setWordSeparator(Delimiters.SPACE).build();
		scoreCtrlParams = new ScoringControlParams.Builder().setScoreNormalizationFactor(1000)
				.setWordSeparator(Delimiters.SPACE).setPluralAlphabet("s").build();

		scoringAlgo = new SimpleStringMatchScoring(scoreCtrlParams);

		strArrQues.add("What is answer1?");
		strArrQues.add("what is Answer2?");
		strArrQues.add("what is Answer3?");
	}

	@Test
	public void testSetInputDataHappyBase() {
		MatchByCommonWords matchingAlgo = new MatchByCommonWords(matchCtrlParams, scoringAlgo);
		matchingAlgo.setInputData(strArrQues, strAns, para);

		assertEquals(strAns, matchingAlgo.getAnswersToCheck());
		assertEquals(para, matchingAlgo.getParaToSearch());
		assertNotSame(strArrQues, matchingAlgo.getQuestionsToAnswer());
		assertEquals(strArrQues, matchingAlgo.getQuestionsToAnswer());
	}

	@Test
	public void testSetInputDataFailBase() {
		MatchByCommonWords matchingAlgo = new MatchByCommonWords(matchCtrlParams, scoringAlgo);
		matchingAlgo.setInputData(strArrQues, strAns, para);

		assertEquals(strAns, matchingAlgo.getAnswersToCheck());
		assertEquals(para, matchingAlgo.getParaToSearch());

		assertNotSame(strArrQues, matchingAlgo.getQuestionsToAnswer());
		assertEquals(strArrQues, matchingAlgo.getQuestionsToAnswer());

		strArrQues.remove(0);
		assertNotEquals(strArrQues, matchingAlgo.getQuestionsToAnswer());
	}

	@Test
	public void testPreProcessInput() {
		MatchByCommonWords matchingAlgo = new MatchByCommonWords(matchCtrlParams, scoringAlgo);
		matchingAlgo.setInputData(strArrQues, strAns, para);
		matchingAlgo.preProcessInput();

		List<String> expectedAns = new ArrayList<String>();
		HashMap<String, String> expectedPara = new HashMap<String, String>();
		HashMap<String, String> expectedQuestions = new HashMap<String, String>();
		expectedQuestions.put("What is answer1?", "answer1");
		expectedQuestions.put("what is Answer2?", "answer2");
		expectedQuestions.put("what is Answer3?", "answer3");

		expectedPara.put("This is answer1 in full", "answer1 full");
		expectedPara.put("This is answer2 in full sentence", "answer2 full sentence");
		expectedPara.put("This is complete answer3", "complete answer3");

		expectedAns.add("is answer1 in full");
		expectedAns.add(" is answer2 in");
		expectedAns.add(" is complete answer3");

		assertTrue(expectedQuestions.equals(matchingAlgo.getProcessedQuestions()));
		assertEquals(expectedPara, matchingAlgo.getProcessedPara());
		assertTrue(expectedPara.equals(matchingAlgo.getProcessedPara()));
		assertEquals(expectedAns, matchingAlgo.getProcessedAnswers());
	}

	@Test
	public void testRunAlgorithm() {
		MatchByCommonWords matchingAlgo = new MatchByCommonWords(matchCtrlParams, scoringAlgo);
		matchingAlgo.setInputData(strArrQues, strAns, para);
		matchingAlgo.preProcessInput();
		matchingAlgo.runAlgorithm();

		HashMap<String, String> matchResult = new HashMap<String, String>();
		
		matchResult.put("What is answer1?", "This is answer1 in full");
		matchResult.put("what is Answer2?", "This is answer2 in full sentence");
		matchResult.put("what is Answer3?", "This is complete answer3");
		
		assertEquals(matchResult, matchingAlgo.getResult());	
	}

}
