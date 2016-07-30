package com.duck8823.model.bot;

import com.google.api.client.util.GenericData;

/**
 * 会話
 * Created by maeda on 7/30/2016.
 */
public class Talk extends GenericData {

	public Talk() {
		this.put("utt", "hello");
	}

	public Talk(String text) {
		this.put("utt", text);
	}

	Talk(String text, String mode, String context) {
		this.put("utt", text);
		this.put("mode", mode);
		this.put("context", context);
	}

	public Talk respond(String text) {
		this.put("utt", text);
		return this;
	}

	public String getContent() {
		return this.get("utt").toString();
	}
}
