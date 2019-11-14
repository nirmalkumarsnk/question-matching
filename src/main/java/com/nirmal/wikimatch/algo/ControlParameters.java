package com.nirmal.wikimatch.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.nirmal.wikimatch.common.Delimiters;
import com.nirmal.wikimatch.exceptions.ValidationException;

public class ControlParameters{

	private final List<String> questionKeyWords;
	private final List<String> fillerWords;

	private final int iNormalizationFactor;
	private final String pluralAlphabet = "s";

	private final List<String> strArrQues;
	private final String strAns;
	private final String strPara;

	private final Delimiters paraDelimiter;
	private final Delimiters ansDelimiter;
	private final Delimiters wordSeparator;
	
	public List<String> getQuestionKeyWords() {
		return questionKeyWords;
	}

	public List<String> getFillerWords() {
		return fillerWords;
	}

	public int getNormalizationFactor() {
		return iNormalizationFactor;
	}

	public String getPluralAlphabet() {
		return pluralAlphabet;
	}

	public List<String> getQuestionArray() {
		return strArrQues;
	}

	public String getScrambledAnswers() {
		return strAns;
	}

	public String getParagraphToSearchFrom() {
		return strPara;
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
	
	private ArrayList<String> strArrProcessedQues;
	private String[] strArrProcessedAns;
	private HashMap<Integer, String> strArrProcessedPara;
	private ArrayList<Integer> matchAnswers = new ArrayList<Integer>();
	private HashMap<String, String> matchResults = new HashMap<String, String>();

	private ControlParameters(MatchingAlgoBuilder builder) {
		this.questionKeyWords = Collections.unmodifiableList(builder.questionKeyWords);
		this.fillerWords = Collections.unmodifiableList(builder.fillerWords);
		this.strArrQues = Collections.unmodifiableList(builder.strArrQues);
		this.strAns = builder.strAns;
		this.strPara = builder.strPara;
		this.paraDelimiter = builder.paraDelimiter;
		this.ansDelimiter = builder.ansDelimiter;
		this.wordSeparator = builder.wordSeparator;
		this.iNormalizationFactor = builder.iNormalizationFactor;
	}

	public void preProcessInput() {
		strArrProcessedQues = preProcessQuestions(strArrQues);
		strArrProcessedAns = preProcessAnswers(strAns);
		strArrProcessedPara = preProcessParagraph(strPara, strArrProcessedAns);
	}

	public HashMap<String, String> runAlgorithm() {

		System.out.println("Questions - ");
		for (String question : strArrProcessedQues) {
			int maxScore = 0;
			int matchAnswer = 0;
			for (int j = 0; j < strArrProcessedPara.size(); j++) {
				String answer = strArrProcessedPara.get(j);
				int matchScore = getMatchScore(question, answer);
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

	private int getMatchScore(String question, String answer) {
		StringTokenizer tokenizer = new StringTokenizer(question, wordSeparator.getDelimiter());
		int totalWordCount = 0;

		int matchWordCount = 0;

		while (tokenizer.hasMoreTokens()) {
			totalWordCount++;
			String word = tokenizer.nextToken().trim();

			if (answer.contains(word)
					|| (word.endsWith(pluralAlphabet) && answer.contains(word.substring(0, word.length() - 1)))) {
				matchWordCount++;
			}
		}
		return (matchWordCount * iNormalizationFactor / totalWordCount);
	}

	private ArrayList<String> preProcessQuestions(List<String> inpArrQues) {
		ArrayList<String> processedArr = new ArrayList<String>();

		for (String strQuestion : inpArrQues) {
			if (strQuestion.endsWith(Delimiters.QUESTION_MARK.getDelimiter())) {
				strQuestion = strQuestion.substring(0, strQuestion.length() - 1);
			}

			StringBuilder processedString = new StringBuilder();
			StringTokenizer tokenizer = new StringTokenizer(strQuestion, wordSeparator.getDelimiter());

			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken().toLowerCase();

				if (!questionKeyWords.contains(token) && !fillerWords.contains(token)) {
					processedString.append(token).append(wordSeparator.getDelimiter());
				}
			}
			processedArr.add(processedString.toString().trim());
		}

		return processedArr;
	}

	private String[] preProcessAnswers(String strAns) {
		String[] strArrAns = strAns.toLowerCase().split(ansDelimiter.getDelimiter());
		return strArrAns;
	}

	private HashMap<Integer, String> preProcessParagraph(String textPara, String[] strArrProcessedAns) {
		HashMap<Integer, String> processedArr = new HashMap<Integer, String>();
		StringTokenizer paraTokenizer = new StringTokenizer(textPara.toLowerCase(), paraDelimiter.getDelimiter());

		while (paraTokenizer.hasMoreElements()) {
			String paraLine = paraTokenizer.nextToken();
			int iPosition = 0;

			for (String processedAns : strArrProcessedAns) {
				if (paraLine.contains(processedAns)) {
					StringBuilder processedString = new StringBuilder();
					StringTokenizer tokenizer = new StringTokenizer(paraLine, wordSeparator.getDelimiter());

					while (tokenizer.hasMoreTokens()) {
						String token = tokenizer.nextToken();

						if (!questionKeyWords.contains(token.toLowerCase())
								&& !fillerWords.contains(token.toLowerCase())) {
							processedString.append(token).append(wordSeparator.getDelimiter());
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

	public static class MatchingAlgoBuilder {

		private int iNormalizationFactor;
		private ArrayList<String> questionKeyWords;
		private ArrayList<String> fillerWords;
		private ArrayList<String> strArrQues;
		private String strAns;
		private String strPara;
		private Delimiters paraDelimiter;
		private Delimiters ansDelimiter;
		private Delimiters wordSeparator;

		public MatchingAlgoBuilder() {
			this.questionKeyWords = new ArrayList<String>();
			this.fillerWords = new ArrayList<String>();
			this.strArrQues = new ArrayList<String>();
			this.strAns = "";
			this.strPara = "";
			this.paraDelimiter = Delimiters.FULL_STOP;
			this.ansDelimiter = Delimiters.SEMI_COLON;
			this.wordSeparator = Delimiters.SPACE;
			this.iNormalizationFactor = 1000;
		}

		public MatchingAlgoBuilder setQuestionWordsToFilter(ArrayList<String> filterWords) {
			if (null != filterWords) {
				this.questionKeyWords.addAll(filterWords);
			}
			return this;
		}

		public MatchingAlgoBuilder setFillerWordsToFilter(ArrayList<String> filterWords) {
			if (null != filterWords) {
				this.fillerWords.addAll(filterWords);
			}
			return this;
		}

		public MatchingAlgoBuilder setQuestions(ArrayList<String> strQuestions) {
			if (null != strQuestions) {
				this.strArrQues.addAll(strQuestions);
			}
			return this;
		}

		public MatchingAlgoBuilder setAnswers(String strAnswer, Delimiters inpDelim) {
			if (null != strAnswer) {
				this.strAns = new String(strAnswer);
				this.ansDelimiter = inpDelim;
			}
			return this;
		}

		public MatchingAlgoBuilder setPara(String strParaText, Delimiters inpDelim) {
			if (null != strParaText) {
				this.strPara = new String(strParaText);
				this.paraDelimiter = inpDelim;
			}
			return this;
		}

		public MatchingAlgoBuilder setWordSeparator(Delimiters inpDelim) {
			if (null != inpDelim) {
				this.wordSeparator = inpDelim;
			}
			return this;
		}

		public MatchingAlgoBuilder setScoreNormalizationFactor(int inputFactor) throws ValidationException {
			if (inputFactor >= 100) {
				this.iNormalizationFactor = inputFactor;
			} else {
				throw new ValidationException("Normalizationfactor should be at least 100");
			}
			return this;
		}

		public ControlParameters build() {
			return new ControlParameters(this);
		}
	}
}