package com.nirmal.wikimatch.driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.nirmal.wikimatch.algo.api.IMatchingAlgorithm;
import com.nirmal.wikimatch.algo.impl.MatchByCommonWords;
import com.nirmal.wikimatch.algo.params.MatchingControlParams;
import com.nirmal.wikimatch.common.Delimiters;
import com.nirmal.wikimatch.exceptions.ValidationException;
import com.nirmal.wikimatch.scoring.impl.SimpleStringMatchScoring;
import com.nirmal.wikimatch.scoring.params.ScoringControlParams;

public class MatchDriver {

	public static void main(String[] args) {		
		ArrayList<String> questionKeyWords = new ArrayList<String>(
				Arrays.asList("why", "who", "what", "which", "when"));
		ArrayList<String> fillerWords = new ArrayList<String>(Arrays.asList("a", "an", "the", "then", "is", "it",
				"of", "are", "to", "some", "their", "by", "and", "that", "in", "but", "such", "as"));
		
		ArrayList<String> strArrQues = new ArrayList<String>();
		String strAns = "subgenus Hippotigris; the plains zebra, the Grévy's zebra and the mountain zebra;horses and donkeys;aims to breed zebras that are phenotypically similar to the quagga; Grévy's zebra and the mountain zebra";
		String para = "Zebras are several species of African equids (horse family) united by their distinctive black and white stripes. Their stripes come in different patterns, unique to each individual. They are generally social animals that live in small harems to large herds. Unlike their closest relatives, horses and donkeys, zebras have never been truly domesticated. There are three species of zebras: the plains zebra, the Grévy's zebra and the mountain zebra. The plains zebra and the mountain zebra belong to the subgenus Hippotigris, but Grévy's zebra is the sole species of subgenus Dolichohippus. The latter resembles an ass, to which it is closely related, while the former two are more horse-like. All three belong to the genus Equus, along with other living equids. The unique stripes of zebras make them one of the animals most familiar to people. They occur in a variety of habitats, such as grasslands, savannas, woodlands, thorny scrublands, mountains, and coastal hills. However, various anthropogenic factors have had a severe impact on zebra populations, in particular hunting for skins and habitat destruction. Grévy's zebra and the mountain zebra are endangered. While plains zebras are much more plentiful, one subspecies - the Quagga - became extinct in the late 19th century. Though there is currently a plan, called the Quagga Project, that aims to breed zebras that are phenotypically similar to the Quagga, in a process called breeding back.";
		
		strArrQues.add("Which Zebras are endangered?");
		strArrQues.add("What is the aim of the Quagga Project?");
		strArrQues.add("Which animals are some of their closest relatives?");
		strArrQues.add("Which are the three species of zebras?");
		strArrQues.add("Which subgenus do the plains zebra and the mountain zebra belong to?");
		
		MatchingControlParams matchCtrlParams;
		ScoringControlParams scoreCtrlParams;
		try {
			matchCtrlParams = new MatchingControlParams.Builder().setAnswerDelimiter(Delimiters.SEMI_COLON).setParagraphDelimiter(Delimiters.FULL_STOP).setFillerWordsToFilter(fillerWords).setQuestionWordsToFilter(questionKeyWords).setWordSeparator(Delimiters.SPACE).build();
			scoreCtrlParams = new ScoringControlParams.Builder().setScoreNormalizationFactor(1000).setWordSeparator(Delimiters.SPACE).setPluralAlphabet("s").build();
			
			SimpleStringMatchScoring scoringAlgo = new SimpleStringMatchScoring(scoreCtrlParams);
			
			IMatchingAlgorithm matchingAlgo = new MatchByCommonWords(matchCtrlParams, scoringAlgo);
			matchingAlgo.setInputData(strArrQues, strAns, para);
			matchingAlgo.preProcessInput();
			
			HashMap<String, String> matchResults = matchingAlgo.runAlgorithm();
			
			for (String question : matchResults.keySet()) {
				
				System.out.println(question + " - " + matchResults.get(question));
			}
			
		} catch (ValidationException e) {
			System.out.println(e.getErrorReason());
			e.printStackTrace();
		}		
	}

}
