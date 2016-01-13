package com.duck8823.context.word;

import lombok.Data;

/**
 * Created by maeda on 2015/12/21.
 */
@Data
public class TokenizedWordCount {

	private String word;

	private Integer count;


	public TokenizedWordCount(String word, Integer count) {
		this.word = word;
		this.count = count;
	}
}
