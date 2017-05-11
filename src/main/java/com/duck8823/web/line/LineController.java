package com.duck8823.web.line;

import com.duck8823.model.bot.BotEnv;
import com.duck8823.model.bot.Talk;
import com.duck8823.model.bot.TalkBot;
import com.duck8823.model.open.weather.map.OpenWeatherMap;
import com.duck8823.model.photo.Photo;
import com.duck8823.service.BotEnvService;
import com.duck8823.service.PhotoService;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

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
	LineMessagingService lineMessagingService;

	@Autowired
	private TalkBot talkBot;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private BotEnvService botEnvService;

	@Autowired
	private Flickr flickr;

	@Autowired
	private OpenWeatherMap openWeatherMap;

	@RequestMapping(path = "callback", method = RequestMethod.POST)
	public void callback(@LineBotMessages List<Event> events) throws IOException, FlickrException {
		log.debug("line bot callback.");
		for (Event event : events) {
			log.debug(event);

			if (event instanceof MessageEvent) {

				String id = event.getSource().getSenderId() != null ? event.getSource().getSenderId() : event.getSource().getUserId();
				Optional<BotEnv> botEnv = botEnvService.findById(id);

				TextMessageContent message = null;
				if (((MessageEvent) event).getMessage() instanceof TextMessageContent) {
					message = (TextMessageContent) ((MessageEvent) event).getMessage();
				} else if (((MessageEvent) event).getMessage() instanceof LocationMessageContent) {
					List<CarouselColumn> columns = new ArrayList<>();

					LocationMessageContent locationMessage = (LocationMessageContent) ((MessageEvent) event).getMessage();
					JSONObject res = openWeatherMap.search(locationMessage.getLatitude(), locationMessage.getLongitude());
					JSONArray list = res.getJSONArray("list");
					for(int i = 0; i < list.length() && i < 5; i++) {
						JSONObject itemJSON = list.getJSONObject(i);
						log.debug(itemJSON);
						JSONObject weatherJSON = itemJSON.getJSONArray("weather").getJSONObject(0);
						String thumbnail = "http://openweathermap.org/img/w/" + weatherJSON.getString("icon") + ".png";
						log.debug(thumbnail);
						columns.add(new CarouselColumn(
								thumbnail,
								itemJSON.getString("dt_txt"),
								weatherJSON.getString("description"),
								Collections.singletonList(
										new MessageAction("hoge", "fuga")
								)
						));
					}

					Response response = lineMessagingService.replyMessage(new ReplyMessage(
							((MessageEvent) event).getReplyToken(),
							new TemplateMessage("template message", new CarouselTemplate(columns))
					)).execute();

					log.debug(response.isSuccessful());
					log.debug(response.message());

					break;
				} else {
					return;
				}


				if (message.getText().contains("wiki")) {
					String searchText = message.getText().replaceAll("(\\s|　)*wiki(\\s|　)*", "");
					if (searchText.isEmpty()) {
						break;
					}
					log.debug(searchText);

					Response response = lineMessagingService.replyMessage(new ReplyMessage(
							((MessageEvent) event).getReplyToken(),
							new TextMessage("https://ja.wikipedia.org/wiki/" + URLEncoder.encode(searchText, "UTF-8"))
					)).execute();

					log.debug(response.isSuccessful());
					log.debug(response.message());

				} else if (message.getText().contains("画像")) {
					String searchText = message.getText().replaceAll("(\\s|　)*画像(\\s|　)*", "");
					if (searchText.isEmpty()) {
						break;
					}
					log.debug(searchText);

					SearchParameters params = new SearchParameters();
					params.setText(searchText);
					params.setSort(SearchParameters.RELEVANCE);

					flickr.getPhotosInterface().search(params, 1, 1).forEach(photo -> {
						try {
							Response response = lineMessagingService.replyMessage(new ReplyMessage(
									((MessageEvent) event).getReplyToken(),
									new ImageMessage(photo.getMediumUrl(), photo.getSmallUrl())
							)).execute();

							log.debug(response.isSuccessful());
							log.debug(response.message());
						} catch (IOException e) {
							throw new IllegalStateException(e);
						}
					});
				} else if (message.getText().contains("しゃべって") || message.getText().contains("喋って")) {
					botEnvService.save(new BotEnv(id, false, null, null));

					Response response = lineMessagingService.replyMessage(new ReplyMessage(
							((MessageEvent) event).getReplyToken(),
							new TextMessage("喋ります!")
					)).execute();

					log.debug(response.isSuccessful());
					log.debug(response.message());

				} else if (botEnv.isPresent() && botEnv.get().getQuiet()) {
					break;
				} else if (message.getText().contains("だまれ") || message.getText().contains("黙れ") || message.getText().contains("静かにして")) {
					botEnvService.save(new BotEnv(id, true, null, null));

					Response response = lineMessagingService.replyMessage(new ReplyMessage(
							((MessageEvent) event).getReplyToken(),
							new TextMessage("黙ります...")
					)).execute();

					log.debug(response.isSuccessful());
					log.debug(response.message());
				} else if (message.getText().contains("写真")) {
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
					BotEnv env = botEnv.orElse(new BotEnv(id));
					Talk talkResponse = talkBot.talk(env.talk().respond(message.getText()));
					env.setContext((String) talkResponse.get("context"));
					env.setMode((String) talkResponse.get("mode"));
					botEnvService.save(env);

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
				switch (((PostbackEvent) event).getPostbackContent().getData()) {
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
