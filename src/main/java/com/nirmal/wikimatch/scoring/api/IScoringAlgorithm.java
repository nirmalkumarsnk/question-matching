package com.nirmal.wikimatch.scoring.api;

public interface IScoringAlgorithm {
	public int getScore(String question, String answer);
}
