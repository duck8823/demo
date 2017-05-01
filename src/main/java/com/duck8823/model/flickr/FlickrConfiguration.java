package com.duck8823.model.flickr;

import com.flickr4java.flickr.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flickrの設定
 * Created by duck8823 on 2017/05/01.
 */
@Configuration
public class FlickrConfiguration {

	@Value("${flickr.api.key}")
	private String key;

	@Value("${flickr.api.secret}")
	private String secret;

	@Bean(name = "flickr")
	public Flickr flickr() {
		return new Flickr(this.key, this.secret, new REST());
	}
}
