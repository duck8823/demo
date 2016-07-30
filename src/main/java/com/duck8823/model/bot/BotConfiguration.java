package com.duck8823.model.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by maeda on 7/30/2016.
 */
@Configuration
public class BotConfiguration {

	@Value("${docomo.talk.api}")
	private String botApi;

	@Bean
	public TalkBot talkBot() {
		return new TalkBot(botApi);
	}
}
