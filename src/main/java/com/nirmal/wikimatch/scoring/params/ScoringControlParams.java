package com.nirmal.wikimatch.scoring.params;

import com.nirmal.wikimatch.common.Delimiters;
import com.nirmal.wikimatch.exceptions.ValidationException;

public class ScoringControlParams {
	private final int iNormalizationFactor;
	private final String pluralAlphabet;
	private final Delimiters wordSeparator;

	public int getNormalizationFactor() {
		return iNormalizationFactor;
	}

	public String getPluralAlphabet() {
		return pluralAlphabet;
	}

	public Delimiters getWordSeparator() {
		return wordSeparator;
	}

	private ScoringControlParams(Builder builder) {
		this.wordSeparator = builder.wordSeparator;
		this.iNormalizationFactor = builder.iNormalizationFactor;
		this.pluralAlphabet = builder.pluralAlphabet;
	}

	public static class Builder {

		public String pluralAlphabet;
		private int iNormalizationFactor;
		private Delimiters wordSeparator;

		public Builder() {
			this.wordSeparator = Delimiters.SPACE;
			this.iNormalizationFactor = 1000;
			this.pluralAlphabet = "s";
		}

		public Builder setPluralAlphabet(String pluralAlphabet) {
			this.pluralAlphabet = pluralAlphabet;
			return this;
		}

		public Builder setWordSeparator(Delimiters inpDelim) {
			if (null != inpDelim) {
				this.wordSeparator = inpDelim;
			}
			return this;
		}

		public Builder setScoreNormalizationFactor(int inputFactor) throws ValidationException {
			if (inputFactor >= 100) {
				this.iNormalizationFactor = inputFactor;
			} else {
				throw new ValidationException("Normalizationfactor should be at least 100");
			}
			return this;
		}

		public ScoringControlParams build() {
			return new ScoringControlParams(this);
		}
	}
}
