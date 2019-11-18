package com.nirmal.wikimatch.scoring.params;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.nirmal.wikimatch.common.Delimiters;
import com.nirmal.wikimatch.exceptions.ValidationException;

public class ScoringControlParamsTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testSuccessfulBuild(){
		try {
			ScoringControlParams scoreCtrlParams = new ScoringControlParams.Builder().setScoreNormalizationFactor(1000)
					.setWordSeparator(Delimiters.SPACE).setPluralAlphabet("s").build();
			
			assertEquals(scoreCtrlParams.normalizationFactor(), 1000);
			assertEquals(scoreCtrlParams.pluralAlphabet(), "s");
			assertEquals(scoreCtrlParams.wordSeparator(), Delimiters.SPACE);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
	@Test(expected=ValidationException.class)
	public void testNormalizationScoreException() throws ValidationException {
			ScoringControlParams scoreCtrlParams = new ScoringControlParams.Builder().setScoreNormalizationFactor(10)
					.setWordSeparator(Delimiters.SPACE).setPluralAlphabet("s").build();

	}
}
