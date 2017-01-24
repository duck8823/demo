package com.duck8823.web.slack;

import com.duck8823.model.bot.BotEnv;
import com.duck8823.model.bot.Talk;
import com.duck8823.model.bot.TalkBot;
import com.duck8823.model.slack.Request;
import com.duck8823.model.slack.Response;
import com.duck8823.service.BotEnvService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

/**
 * Slack Interactive Messages を試す
 * Created by maeda on 10/3/2016.
 */
@Transactional
@Log4j
@RequestMapping("slack")
@RestController
public class SlackController {

	@Autowired
	private TalkBot talkBot;

	@Autowired
	private BotEnvService botEnvService;

	@RequestMapping(path = "callback", method = RequestMethod.POST)
	public ResponseEntity<Response> callback(Request request) throws IOException {
		log.debug(request);
		Optional<BotEnv> botEnv = botEnvService.findById(request.getChannel_id());

		if (request.getUser_id().matches(".*?SLACKBOT.*?")) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (request.getText().contains("しゃべって") || request.getText().contains("喋って")) {
			botEnvService.save(new BotEnv(request.getChannel_id(), false, null, null));

			return new ResponseEntity<>(new Response("喋ります！"), HttpStatus.OK);
		} else if (botEnv.isPresent() && botEnv.get().getQuiet()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (request.getText().contains("だまれ") || request.getText().contains("静かにして")) {
			botEnvService.save(new BotEnv(request.getChannel_id(), true, null, null));

			return new ResponseEntity<>(new Response("黙ります..."), HttpStatus.OK);
		}
		BotEnv env = botEnv.orElse(new BotEnv(request.getChannel_id(), false, null, null));
		Talk talkResponse = talkBot.talk(env.talk().respond(request.getText()));
		env.setContext((String) talkResponse.get("context"));
		env.setMode((String) talkResponse.get("mode"));
		botEnvService.save(env);

		return new ResponseEntity<>(new Response(talkResponse.getContent()), HttpStatus.OK);
	}
}
