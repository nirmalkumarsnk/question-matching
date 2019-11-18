package com.nirmal.wikimatch.common;

import java.util.List;
import java.util.StringTokenizer;

public class StringProcessingUtils {
	/* Returns a string with the 'noise' strings removed from 'input', delimited by 'token' */
	public static String tokenizeAndCleanseString(String input, String token, List<String> noise) {
		StringBuilder cleanString = new StringBuilder();
		StringTokenizer tokenizer = new StringTokenizer(input, token);

		while (tokenizer.hasMoreTokens()) {
			String tokenStr = tokenizer.nextToken().toLowerCase();
			if (!noise.contains(tokenStr)) {
				cleanString.append(tokenStr).append(token);
			}
		}
		return cleanString.toString().trim();
	}
}
