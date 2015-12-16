package net.duck8823.model.tweeet;

import net.duck8823.model.photo.Photo;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by maeda on 2015/12/17.
 */
public class Tweet {

	private Twitter twitter;

	private String message;

	private Photo photo;

	public Tweet(String message){
		this.twitter = new TwitterFactory().getInstance();
		this.message = message;
	}
	public Tweet(String message, Photo photo){
		this(message);
		this.photo = photo;
	}

	public void post() throws TwitterException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		String date = sdf.format(new Date(System.currentTimeMillis()));
		StatusUpdate status = new StatusUpdate(date + "\n" + message);
		if(photo != null){
			status.setMedia("photo", new ByteArrayInputStream(photo.getImage()));
		}
		twitter.updateStatus(status);
	}
}
