package com.duck8823.web.line;

import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.model.callback.Event;
import com.linecorp.bot.model.content.Content;
import com.linecorp.bot.model.content.TextContent;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * LINE BOT をためす
 * Created by maeda on 7/30/2016.
 */
@RequestMapping("line")
@Controller
public class LineController {

	@Autowired
	private LineBotClient lineBotClient;

	@RequestMapping(path = "callback", method = RequestMethod.POST)
	public void callback(@LineBotMessages List<Event> events) throws LineBotAPIException {
		for (Event event : events) {
			Content content = event.getContent();
			if (content instanceof TextContent) {
				TextContent text = (TextContent) content;
				lineBotClient.sendText(text.getText(), text.getFrom());
			}
		}
	}
}
