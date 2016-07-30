package com.duck8823.web.line;

import com.duck8823.model.bot.Talk;
import com.duck8823.model.bot.TalkBot;
import com.duck8823.model.photo.Photo;
import com.duck8823.service.PhotoService;
import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.model.callback.Event;
import com.linecorp.bot.model.callback.EventType;
import com.linecorp.bot.model.callback.OperationEvent;
import com.linecorp.bot.model.content.AddedAsFriendOperation;
import com.linecorp.bot.model.content.Content;
import com.linecorp.bot.model.content.TextContent;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * LINE BOT をためす
 * Created by maeda on 7/30/2016.
 */
@SessionAttributes({"talk"})
@Log4j
@RequestMapping("line")
@RestController
public class LineController {

	@Autowired
	private LineBotClient lineBotClient;

	@Autowired
	private TalkBot talkBot;

	@Autowired
	private PhotoService photoService;

	@ModelAttribute
	private Talk talk() {
		return new Talk();
	}

	@RequestMapping(path = "callback", method = RequestMethod.POST)
	public void callback(@LineBotMessages List<Event> events, @ModelAttribute Talk talk) throws LineBotAPIException, IOException {
		for (Event event : events) {
			Content content = event.getContent();
			if (content instanceof TextContent) {
				TextContent text = (TextContent) content;

				if (text.getText().contains("写真")){
					Photo photo = photoService.random().get();
					String url = "https://www.duck8823.com/photo/" + photo.getId();
					lineBotClient.sendImage(text.getFrom(), url, url);
				} else {
					Talk response = talkBot.talk(talk.respond(text.getText()));

					lineBotClient.sendText(text.getFrom(), response.getContent());
				}
			} else if (content instanceof AddedAsFriendOperation) {
				AddedAsFriendOperation operation = (AddedAsFriendOperation) content;

				lineBotClient.sendText(operation.getMid(), "Docomo Developer supportのAPIを利用しています.\n会話の内容はドコモのサーバに送信されます.");
			}
		}
	}
}
