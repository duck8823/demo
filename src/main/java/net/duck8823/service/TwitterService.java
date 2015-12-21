package net.duck8823.service;

import lombok.extern.log4j.Log4j;
import net.duck8823.model.twitter.TweetMedia;
import net.duck8823.model.twitter.TokenizedWordCount;
import net.duck8823.model.twitter.TokenizedWordCountList;
import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.util.*;

/**
 * Created by maeda on 2015/12/21.
 */
@Log4j
@Service
public class TwitterService {

	private static final int PARTS_OF_SPEECH_INDEX = 0;
	private static final String PARTS_OF_SPEECH_REGEX = "名詞|形容詞"; //|動詞|形容動詞";
	private static final String IGNORE_REGEX = "http(s)?://[^\\s]*|@[^\\s]*|\\n";

	public TokenizedWordCountList getTokenizedWordCountMap(Twitter twitter) throws TwitterException {
		Tokenizer tokenizer = Tokenizer.builder().build();
		List<Token> tokens = tokenizer.tokenize(getText(twitter));
		return getCountList(tokens);
	}

	public List<TweetMedia> getTweetMedias(Twitter twitter) throws TwitterException {
		List<TweetMedia> tweetMedias = new ArrayList<>();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);

		boolean oneYearAgo = false;
		Paging paging = new Paging();
		paging.setCount(200);
		paging.setPage(1);
		while(!oneYearAgo){
			ResponseList<Status> statuses = twitter.timelines().getUserTimeline(twitter.getScreenName(), paging);
			if(statuses.isEmpty()){
				break;
			}
			for(Status status : statuses){
				if(status.getCreatedAt().getTime() >= calendar.getTimeInMillis()){
					for(MediaEntity mediaEntity : status.getMediaEntities()){
						if(!mediaEntity.getType().equals("photo")){
							continue;
						}
						tweetMedias.add(new TweetMedia(mediaEntity.getMediaURL(), status.getCreatedAt()));
					}
				} else {
					oneYearAgo = true;
					break;
				}
				paging.setMaxId(status.getId());
			}
			log.debug("Page : " + paging.getPage());
			paging.setPage(paging.getPage() + 1);
		}
		return tweetMedias;
	}

	private String getText(Twitter twitter) throws TwitterException {
		StringBuffer sb = new StringBuffer();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);

		log.debug(twitter.getScreenName());

		boolean oneYearAgo = false;
		Paging paging = new Paging();
		paging.setCount(200);
		paging.setPage(1);
		while(!oneYearAgo){
			ResponseList<Status> statuses = twitter.timelines().getUserTimeline(twitter.getScreenName(), paging);
			if(statuses.isEmpty()){
				break;
			}
			for(Status status : statuses){
				if(status.getCreatedAt().getTime() >= calendar.getTimeInMillis()){
					sb.append(status.getText()).append(" ");
				} else {
					oneYearAgo = true;
					break;
				}
				paging.setMaxId(status.getId());
			}
			log.debug("Page : " + paging.getPage());
			paging.setPage(paging.getPage() + 1);
		}
		return sb.toString().replaceAll(IGNORE_REGEX, " ").replaceAll("\\s+", " ");
	}

	private TokenizedWordCountList getCountList(List<Token> tokens) {
		TokenizedWordCountList words = new TokenizedWordCountList();

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
