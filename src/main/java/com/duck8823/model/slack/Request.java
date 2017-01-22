package com.duck8823.model.slack;

import lombok.Data;

/**
 * SlackのRequest
 * Created by maeda on 1/22/2017.
 */
@Data
public class Request {

	String token;

	String team_id;

	String team_domain;

	String channel_id;

	String channel_name;

	String timestamp;

	String user_id;

	String user_name;

	String text;

	String trigger_word;
}
