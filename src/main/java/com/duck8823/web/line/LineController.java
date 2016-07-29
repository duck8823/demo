package com.duck8823.web.line;

import com.duck8823.model.photo.Photo;
import com.duck8823.service.PhotoService;
import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.model.callback.Event;
import com.linecorp.bot.model.content.Content;
import com.linecorp.bot.model.content.TextContent;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

/**
 * LINE BOT をためす
 * Created by maeda on 7/30/2016.
 */
@Transactional
@Log4j
@RequestMapping("line")
@RestController
public class LineController {

	@Autowired
	private LineBotClient lineBotClient;

	@Autowired
	private PhotoService photoService;

	@RequestMapping(path = "callback", method = RequestMethod.POST)
	public void callback(@LineBotMessages List<Event> events) throws LineBotAPIException {
		for (Event event : events) {
			Content content = event.getContent();
			if (content instanceof TextContent) {
				TextContent text = (TextContent) content;
				Photo photo = photoService.random().get();
				String url = "https://www.duck8823.com/photo/" + photo.getId();
				lineBotClient.sendImage(text.getFrom(), url, url);
			}
		}
	}
}
