package net.duck8823.context.word;

import lombok.extern.log4j.Log4j;
import org.atilika.kuromoji.Token;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by maeda on 2015/12/21.
 */
@Log4j
@Component
public class WordHandler {

	private static final int PARTS_OF_SPEECH_INDEX = 0;
	private static final String PARTS_OF_SPEECH_REGEX = "名詞|形容詞"; //|動詞|形容動詞";

	public List<TokenizedWordCount> getCountList(List<Token> tokens) {
		List<TokenizedWordCount> words = new ArrayList<>();

		Map<String,Integer> wordMap = new HashMap<>();
		for(Token token : tokens){
			if(!token.getAllFeaturesArray()[PARTS_OF_SPEECH_INDEX].matches(PARTS_OF_SPEECH_REGEX) && token.isKnown()) {
				continue;
			}
			String word = token.getSurfaceForm();
			if(word.length() <= 1){
				continue;
			}
			if(!wordMap.containsKey(word)){
				wordMap.put(word, 1);
			} else {
				wordMap.put(word, wordMap.get(word) + 1);
			}
		}
		List<Map.Entry<String,Integer>> entries = new ArrayList<>(wordMap.entrySet());
		Collections.sort(entries, (entry1, entry2) -> (entry2.getValue()).compareTo(entry1.getValue()));
		for(Map.Entry<String, Integer> entry : entries){
			words.add(new TokenizedWordCount(entry.getKey(), entry.getValue()));
			if(words.size() >= 250){
				break;
			}
		}
		return words;
	}
}
