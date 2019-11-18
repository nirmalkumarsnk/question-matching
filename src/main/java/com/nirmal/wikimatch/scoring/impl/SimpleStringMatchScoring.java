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
	
	public int getScore(String question, String answer) {
		StringTokenizer tokenizer = new StringTokenizer(question, ctrlParams.getWordSeparator().getValue());
		int totalWordCount = 0;

		int matchWordCount = 0;

		while (tokenizer.hasMoreTokens()) {
			totalWordCount++;
			String word = tokenizer.nextToken().trim();

			if (answer.contains(word) || (word.endsWith(ctrlParams.getPluralAlphabet())
					&& answer.contains(word.substring(0, word.length() - 1)))) {
				matchWordCount++;
			}
		}
		return (matchWordCount * ctrlParams.getNormalizationFactor() / totalWordCount);
	}
}
