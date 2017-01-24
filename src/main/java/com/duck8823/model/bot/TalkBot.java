package com.duck8823.model.bot;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.log4j.Log4j;
import net.arnx.jsonic.JSON;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Docomoの雑談対話Bot
 * Created by maeda on 7/30/2016.
 */
@Log4j
public class TalkBot {

	private final String END_POINT = "https://api.apigw.smt.docomo.ne.jp/dialogue/v1/dialogue?APIKEY=";

	private HttpTransport httpTransport;

	private String apiKey;

	public TalkBot(String apiKey){
		this.httpTransport = new NetHttpTransport();
		this.apiKey = apiKey;
	}

	public Talk talk(Talk talk) throws IOException {
		log.debug(talk);
		HttpContent content = new JsonHttpContent(new JacksonFactory(), talk.toMap());
		log.debug(content);

		HttpRequest request = httpTransport.createRequestFactory().buildPostRequest(new GenericUrl(END_POINT + apiKey), content);
		Map<String, String> response = JSON.decode(request.execute().getContent());
		log.debug(response);

		return new Talk(response.get("utt"), response.get("mode"), response.get("context"));
	}

}
