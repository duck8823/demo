package com.duck8823.model.twitter;

import com.duck8823.model.photo.Photo;
import lombok.Getter;

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
