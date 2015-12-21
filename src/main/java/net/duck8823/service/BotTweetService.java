package net.duck8823.service;

import net.duck8823.model.twitter.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.ByteArrayInputStream;

/**
 * Created by maeda on 2015/12/21.
 */
@Service
public class BotTweetService {

	@Qualifier("bot")
	@Autowired
	private Twitter twitter;

	public void post(Tweet tweet) throws TwitterException {
		String message = "";
		if(StringUtils.isEmpty(tweet.getTimestamp())){
			message = tweet.getTimestamp();
		}
		message += tweet.getMessage();
		StatusUpdate status = new StatusUpdate(message);
		if(tweet.getPhoto() != null){
			status.setMedia("photo", new ByteArrayInputStream(tweet.getPhoto().getImage()));
		}
		twitter.updateStatus(status);
	}
}
