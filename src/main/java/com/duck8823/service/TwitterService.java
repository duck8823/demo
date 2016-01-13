package com.duck8823.service;

import com.duck8823.context.media.Media;
import lombok.extern.log4j.Log4j;
import com.duck8823.context.word.TokenizedWordCount;
import com.duck8823.context.word.WordHandler;
import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by maeda on 2015/12/23.
 */
@Log4j
@Service
public class TwitterService {

	private static final String IGNORE_REGEX = "http(s)?://[^\\s]*|@[^\\s]*|\\n";

	@Autowired
	private WordHandler wordHandler;

	public List<TokenizedWordCount> getTokenizedWordCountMap(Twitter twitter) throws TwitterException {
		Tokenizer tokenizer = Tokenizer.builder().build();
		List<Token> tokens = tokenizer.tokenize(getText(twitter));
		return Collections.unmodifiableList(wordHandler.getCountList(tokens));
	}

	public List<Media> getTweetMedias(Twitter twitter) throws TwitterException {
		List<Media> medias = new ArrayList<>();

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
						medias.add(new Media(mediaEntity.getMediaURL(), status.getCreatedAt()));
					}
				} else {
					oneYearAgo = true;
					break;
				}
			}
			log.debug("Page : " + paging.getPage());
			paging.setPage(paging.getPage() + 1);
		}
		return Collections.unmodifiableList(medias);
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
			}
			log.debug("Page : " + paging.getPage());
			paging.setPage(paging.getPage() + 1);
		}
		return sb.toString().replaceAll(IGNORE_REGEX, " ").replaceAll("\\s+", " ");
	}
}
