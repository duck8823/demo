package com.duck8823.model.bot;

import com.google.api.client.util.GenericData;

import java.util.HashMap;
import java.util.Map;

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

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("utt", this.get("utt"));
		map.put("mode", this.get("mode"));
		map.put("context", this.get("context"));
		return map;
	}

	public String getContent() {
		return this.get("utt").toString();
	}
}
