package com.duck8823.model.facebook;

import com.duck8823.model.photo.Photos;
import lombok.Getter;

/**
 * Created by maeda on 2016/01/21.
 */
@Getter
public class Post {

	private String message;

	private Photos photos;

	public Post(String message, Photos photos) {
		this.message = message;
		this.photos = photos;
	}
}
