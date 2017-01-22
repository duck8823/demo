package com.duck8823.web.slack;

import com.duck8823.model.bot.Talk;
import com.duck8823.model.bot.TalkBot;
import com.duck8823.model.slack.Request;
import com.duck8823.model.slack.Response;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Slack Interactive Messages を試す
 * Created by maeda on 10/3/2016.
 */
@Log4j
@RequestMapping("slack")
@RestController
public class SlackController {

	@Autowired
	private TalkBot talkBot;

	@ModelAttribute
	private Talk talk() {
		return new Talk();
	}

	@RequestMapping(path = "callback", method = RequestMethod.POST)
	public ResponseEntity<Response> callback(Request request, @ModelAttribute Talk talk) throws IOException {
		log.debug(request);
		if (request.getUser_id().matches(".*?SLACKBOT.*?")) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Talk talkResponse = talkBot.talk(talk.respond(request.getText()));
		return new ResponseEntity<>(new Response(talkResponse.getContent()), HttpStatus.OK);
	}
}
