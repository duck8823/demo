package com.duck8823;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by maeda on 2016/02/15.
 */
@Configuration
public class TestConfiguration {

	@Bean
	public Twitter twitter() throws TwitterException {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey("VPH1ipNmHLKQhhBDAyDUVKECq")
				.setOAuthConsumerSecret("FqrozKoCScLaY5hmtGaz4kRiJ3cHY6QXZ6StJWg7QlYjkMekjE")
				.setOAuthAccessToken("4880192112-qL6ugncGOIVTy1Wq61a9MLqOgzqd7EQRyl2ZUUB")
				.setOAuthAccessTokenSecret("ZEoh4zsNYE07hbb0rZPurAXH0bLv8GuaMyEOgIQFxL8M5");
		return new TwitterFactory().getInstance(AuthorizationFactory.getInstance(builder.build()));
	}
}
