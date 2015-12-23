package net.duck8823.model.twitter;

import lombok.Getter;
import net.duck8823.model.photo.Photo;
import org.thymeleaf.util.StringUtils;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by maeda on 2015/12/17.
 */
@Getter
public class Tweet {

	private Date timestamp;

	private String message;

	private Photo photo;

	public Tweet(String message) {
		this.message = message;
	}
	public Tweet(String message, Photo photo){
		this(message);
		this.photo = photo;
	}

	public void touch() {
		this.timestamp =new Date(System.currentTimeMillis());
	}

}
