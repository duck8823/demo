package com.duck8823.web.line;

import com.duck8823.model.bot.BotEnv;
import com.duck8823.model.bot.Talk;
import com.duck8823.model.bot.TalkBot;
import com.duck8823.model.photo.Photo;
import com.duck8823.service.BotEnvService;
import com.duck8823.service.PhotoService;
import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * LINE BOT をためす
 * Created by maeda on 7/30/2016.
 */
@Transactional
@SessionAttributes({"talk"})
@Log4j
@RequestMapping("line")
@RestController
public class LineController {

	@Autowired
	LineMessagingService lineMessagingService;

	@Autowired
	private TalkBot talkBot;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private BotEnvService botEnvService;

	@ModelAttribute
	private Talk talk() {
		return new Talk();
	}

	@RequestMapping(path = "callback", method = RequestMethod.POST)
	public void callback(@LineBotMessages List<Event> events, @ModelAttribute Talk talk) throws IOException {
		log.debug("line bot callback.");
		for (Event event : events) {
			log.debug(event);

			if (event instanceof MessageEvent) {

				Optional<BotEnv> botEnv = botEnvService.findById(event.getSource().getSenderId());

				MessageContent message = ((MessageEvent) event).getMessage();

				if (message.toString().contains("しゃべって") || message.toString().contains("喋って")) {
					botEnvService.save(new BotEnv(event.getSource().getSenderId(), false));

					Response response = lineMessagingService.replyMessage(new ReplyMessage(
							((MessageEvent) event).getReplyToken(),
							new TextMessage("喋ります!")
					)).execute();

					log.debug(response.isSuccessful());
					log.debug(response.message());

				} else if (botEnv.isPresent() && botEnv.get().getQuiet()) {
					break;
				} else if (message.toString().contains("だまれ") || message.toString().contains("静かにして")) {
					botEnvService.save(new BotEnv(event.getSource().getSenderId(), true));

					Response response = lineMessagingService.replyMessage(new ReplyMessage(
							((MessageEvent) event).getReplyToken(),
							new TextMessage("黙ります...")
					)).execute();

					log.debug(response.isSuccessful());
					log.debug(response.message());
				} else if (message.toString().contains("写真")) {
					Response response = lineMessagingService.replyMessage(new ReplyMessage(
							((MessageEvent) event).getReplyToken(),
							new TemplateMessage("写真",
									new ConfirmTemplate("写真欲しいですか？",
											Arrays.asList(
													new PostbackAction("欲しい", "photo"),
													new MessageAction("いらない.", "いらない.")
											)
									)
							)
					)).execute();

					log.debug(response.isSuccessful());
					log.debug(response.message());
				} else {
					Talk talkResponse = talkBot.talk(talk.respond(message.toString()));

					Response response = lineMessagingService.replyMessage(
							new ReplyMessage(
									((MessageEvent) event).getReplyToken(),
									new TextMessage(talkResponse.getContent())
							)).execute();
					log.debug(response.isSuccessful());
					log.debug(response.message());
				}
			} else if (event instanceof FollowEvent) {
				lineMessagingService.replyMessage(
						new ReplyMessage(
								((FollowEvent) event).getReplyToken(),
								new TextMessage("このBotは docomo Developer supportのAPIを利用しています.\n会話の内容はドコモのサーバに送信されます.")
						)).execute();
			} else if (event instanceof JoinEvent) {
				lineMessagingService.replyMessage(
						new ReplyMessage(
								((JoinEvent) event).getReplyToken(),
								new TextMessage("このBotは docomo Developer supportのAPIを利用しています.\n会話の内容はドコモのサーバに送信されます.")
						)).execute();
			} else if (event instanceof PostbackEvent) {
				switch (((PostbackEvent) event).getPostbackContent().getData()){
					case "photo":
						Photo photo = photoService.random().get();
						String url = "https://www.duck8823.com/photo/" + photo.getId();

						Response response = lineMessagingService.replyMessage(
								new ReplyMessage(
										((PostbackEvent) event).getReplyToken(),
										new ImageMessage(url, url)
								)).execute();
						log.debug(response.isSuccessful());
						log.debug(response.message());
						break;
				}
			}
		}
	}
}
