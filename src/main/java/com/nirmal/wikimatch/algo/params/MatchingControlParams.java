package com.nirmal.wikimatch.algo.params;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.nirmal.wikimatch.common.Delimiters;

public class MatchingControlParams{

	private final List<String> questionKeyWords;
	private final List<String> fillerWords;

	private final Delimiters paraDelimiter;
	private final Delimiters ansDelimiter;
	private final Delimiters wordSeparator;
	
	public List<String> getQuestionKeyWords() {
		return questionKeyWords;
	}

	public List<String> getFillerWords() {
		return fillerWords;
	}

	public Delimiters getParagraphDelimiter() {
		return paraDelimiter;
	}

	public Delimiters getAnswerDelimiter() {
		return ansDelimiter;
	}

	public Delimiters getWordSeparator() {
		return wordSeparator;
	}
	
	private MatchingControlParams(Builder builder) {
		this.questionKeyWords = Collections.unmodifiableList(builder.questionKeyWords);
		this.fillerWords = Collections.unmodifiableList(builder.fillerWords);
		this.paraDelimiter = builder.paraDelimiter;
		this.ansDelimiter = builder.ansDelimiter;
		this.wordSeparator = builder.wordSeparator;
	}

	public static class Builder {

		private ArrayList<String> questionKeyWords;
		private ArrayList<String> fillerWords;
		private Delimiters paraDelimiter;
		private Delimiters ansDelimiter;
		private Delimiters wordSeparator;

		public Builder() {
			this.questionKeyWords = new ArrayList<String>();
			this.fillerWords = new ArrayList<String>();
			this.paraDelimiter = Delimiters.FULL_STOP;
			this.ansDelimiter = Delimiters.SEMI_COLON;
			this.wordSeparator = Delimiters.SPACE;
		}

		public Builder setQuestionWordsToFilter(ArrayList<String> filterWords) {
			if (null != filterWords) {
				this.questionKeyWords.addAll(filterWords);
			}
			return this;
		}

		public Builder setFillerWordsToFilter(ArrayList<String> filterWords) {
			if (null != filterWords) {
				this.fillerWords.addAll(filterWords);
			}
			return this;
		}
		
		public Builder setWordSeparator(Delimiters inpDelim) {
			if (null != inpDelim) {
				this.wordSeparator = inpDelim;
			}
			return this;
		}

		public Builder setAnswerDelimiter(Delimiters delim) {
			this.ansDelimiter = delim;
			return this;
		}

		public Builder setParagraphDelimiter(Delimiters delim) {
			this.paraDelimiter = delim;
			return this;
		}
		
		public MatchingControlParams build() {
			return new MatchingControlParams(this);
		}
	}
}