package net.duck8823.model.twitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by maeda on 2015/12/21.
 */
@Configuration
public class TwitterConfiguration {

	@Value("${bot.oauth.consumerKey}")
	private String consumerKey;

	@Value("${bot.oauth.consumerSecret}")
	private String consumerSecret;

	@Value("${bot.oauth.accessToken}")
	private String accessToken;

	@Value("${bot.oauth.accessTokenSecret}")
	private String accessTokenSecret;

	@Bean(name = "bot")
	public Twitter twitter(){
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(consumerKey);
		builder.setOAuthConsumerSecret(consumerSecret);
		builder.setOAuthAccessToken(accessToken);
		builder.setOAuthAccessTokenSecret(accessTokenSecret);
		return new TwitterFactory().getInstance(AuthorizationFactory.getInstance(builder.build()));
	}
}
