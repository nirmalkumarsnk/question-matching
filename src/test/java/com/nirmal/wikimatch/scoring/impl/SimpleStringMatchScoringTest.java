package com.nirmal.wikimatch.scoring.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.nirmal.wikimatch.common.Delimiters;
import com.nirmal.wikimatch.scoring.params.ScoringControlParams;

public class SimpleStringMatchScoringTest {

	private ScoringControlParams scoreCtrlParams;
	private SimpleStringMatchScoring scorer;

	@Before
	public void setUp() throws Exception {
		scoreCtrlParams = new ScoringControlParams.Builder().setScoreNormalizationFactor(1000)
				.setWordSeparator(Delimiters.SPACE).setPluralAlphabet("s").build();
		scorer = new SimpleStringMatchScoring(scoreCtrlParams);
	}

	@Test
	public void testSuccessfulScoring() {
		String ques = "which is the second most populated country in the world";
		String ans = "india is the second most populated country in the world";
		
		assertEquals(900, scorer.getScore(ques, ans));
		
		ques = "first second and third";		
		assertEquals(250, scorer.getScore(ques, ans));
		
		ques = "himalayas are great range of mountains";		
		assertEquals(0, scorer.getScore(ques, ans));
		
		ques = "";
		assertEquals(0, scorer.getScore(ques, ans));
		
		ques = "Runs scored in a day";
		ans = "Run scored in a day";
		assertEquals(1000, scorer.getScore(ques, ans));
		
		ques = "Run scored in a day";
		ans = "Runs scored in a day";
		assertEquals(1000, scorer.getScore(ques, ans));
		
		ans = "";
		assertEquals(0, scorer.getScore(ques, ans));
		
		ques = "Run scored in a day by a player";
		ans = "Runs scored in a day";
		assertEquals(750, scorer.getScore(ques, ans));
	}

}
