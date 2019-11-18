package com.nirmal.wikimatch.scoring.impl;

import java.util.StringTokenizer;

import com.nirmal.wikimatch.scoring.api.IScoringAlgorithm;
import com.nirmal.wikimatch.scoring.params.ScoringControlParams;

public class SimpleStringMatchScoring implements IScoringAlgorithm {
	private ScoringControlParams ctrlParams;
	
	public SimpleStringMatchScoring(ScoringControlParams params)
	{
		this.ctrlParams = params;
	}
	
	/******************************************************************
	 * Calculates the match score as a ratio of the total number of
	 * words in the question string to the actual number of words that
	 * matched to the answer string.
	 * This is a very simple algorithm and not perfect. Some known
	 * scenarios where it would not give perfect answers are:
	 *	- If the answer contains words that are super-strings of certain
	 *		words in the question
	 *	- If there are apostropohe's
	 * ***************************************************************/
	public int getScore(String question, String answer) {
		
		if(question.length() == 0 || answer.length() == 0)
		{
			return 0;
		}
		
		StringTokenizer tokenizer = new StringTokenizer(question, ctrlParams.wordSeparator().getValue());
		int totalWordCount = 0;

		int matchWordCount = 0;

		while (tokenizer.hasMoreTokens()) {
			totalWordCount++;
			String word = tokenizer.nextToken().trim();

			if (answer.contains(word) || (word.endsWith(ctrlParams.pluralAlphabet())
					&& answer.contains(word.substring(0, word.length() - 1)))) {
				matchWordCount++;
			}
		}
		return (matchWordCount * ctrlParams.normalizationFactor() / totalWordCount);
	}
}
