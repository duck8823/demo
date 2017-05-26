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
import com.flickr4java.flickr.photos.SearchParameters;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.*;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.singletonList;

/**
 * LINE BOT をためす
 * Created by maeda on 7/30/2016.
 */
@Transactional
@Log4j
@LineMessageHandler
public class LineController {

	@Autowired
	private LineMessagingClient lineMessagingClient;

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

	@EventMapping
	public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) throws IOException, ParseException, ExecutionException, InterruptedException {
		List<CarouselColumn> columns = new ArrayList<>();

		LocationMessageContent locationMessage = event.getMessage();
		JSONObject res = openWeatherMap.search(locationMessage.getLatitude(), locationMessage.getLongitude());
		JSONArray list = res.getJSONArray("list");
		for(int i = 0; i < list.length() && i < 5; i++) {
			JSONObject itemJSON = list.getJSONObject(i);
			log.debug(itemJSON);
			JSONObject weatherJSON = itemJSON.getJSONArray("weather").getJSONObject(0);
			String thumbnail = "https://openweathermap.org/img/w/" + weatherJSON.getString("icon") + ".png";
			log.debug(thumbnail);


			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = sdf.parse(itemJSON.getString("dt_txt"));

			sdf.setTimeZone(TimeZone.getTimeZone("JST"));
			String dateTxt = sdf.format(date);

			columns.add(new CarouselColumn(
					thumbnail,
					dateTxt,
					String.join("\n",
							weatherJSON.getString("description"),
							"temp: " + itemJSON.getJSONObject("main").getDouble("temp"),
							"humidity: " + itemJSON.getJSONObject("main").getDouble("humidity")
					),
					singletonList(
							new MessageAction("画像", weatherJSON.getString("description") + " 画像")
					)
			));
		}

		BotApiResponse response = lineMessagingClient.replyMessage(new ReplyMessage(
				event.getReplyToken(),
				new TemplateMessage("template message", new CarouselTemplate(columns))
		)).get();

		log.debug(response.getMessage());
	}

	@EventMapping
	public void handlePostbackEvent(PostbackEvent event) throws IOException, ExecutionException, InterruptedException {
		switch (event.getPostbackContent().getData()) {
			case "photo":
				Photo photo = photoService.random().get();
				String url = "https://www.duck8823.com/photo/" + photo.getId();

				BotApiResponse response = lineMessagingClient.replyMessage(
						new ReplyMessage(
								event.getReplyToken(),
								new ImageMessage(url, url)
						)).get();
				log.debug(response.getMessage());
				break;
		}
	}

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws ExecutionException, InterruptedException, IOException, FlickrException {
		String id = event.getSource().getSenderId() != null ? event.getSource().getSenderId() : event.getSource().getUserId();
		Optional<BotEnv> botEnv = botEnvService.findById(id);

		TextMessageContent message = event.getMessage();

		if (message.getText().contains("wiki")) {
			String searchText = message.getText().replaceAll("(\\s|　)*wiki(\\s|　)*", "");
			if (searchText.isEmpty()) {
				return;
			}
			log.debug(searchText);

			BotApiResponse response = lineMessagingClient.replyMessage(new ReplyMessage(
					event.getReplyToken(),
					new TextMessage("https://ja.wikipedia.org/wiki/" + URLEncoder.encode(searchText, "UTF-8"))
			)).get();

			log.debug(response.getMessage());

		} else if (message.getText().contains("画像")) {
			String searchText = message.getText().replaceAll("(\\s|　)*画像(\\s|　)*", "");
			if (searchText.isEmpty()) {
				return;
			}
			log.debug(searchText);

			SearchParameters params = new SearchParameters();
			params.setText(searchText);
			params.setSort(SearchParameters.RELEVANCE);

			flickr.getPhotosInterface().search(params, 1, 1).forEach(photo -> {
				try {
					BotApiResponse response = lineMessagingClient.replyMessage(new ReplyMessage(
							event.getReplyToken(),
							new ImageMessage(photo.getMediumUrl(), photo.getSmallUrl())
					)).get();

					log.debug(response.getMessage());
				} catch (InterruptedException | ExecutionException e) {
					log.error("error", e);
				}
			});
		} else if (message.getText().contains("しゃべって") || message.getText().contains("喋って")) {
			botEnvService.save(new BotEnv(id, false, null, null));

			BotApiResponse response = lineMessagingClient.replyMessage(new ReplyMessage(
					event.getReplyToken(),
					new TextMessage("喋ります!")
			)).get();

			log.debug(response.getMessage());

		} else if (botEnv.isPresent() && botEnv.get().getQuiet()) {
			return;
		} else if (message.getText().contains("だまれ") || message.getText().contains("黙れ") || message.getText().contains("静かにして")) {
			botEnvService.save(new BotEnv(id, true, null, null));

			BotApiResponse response = lineMessagingClient.replyMessage(new ReplyMessage(
					event.getReplyToken(),
					new TextMessage("黙ります...")
			)).get();

			log.debug(response.getMessage());
		} else if (message.getText().contains("写真")) {
			BotApiResponse response = lineMessagingClient.replyMessage(new ReplyMessage(
					event.getReplyToken(),
					new TemplateMessage("写真",
							new ConfirmTemplate("写真欲しいですか？",
									Arrays.asList(
											new PostbackAction("欲しい", "photo"),
											new MessageAction("いらない.", "いらない.")
									)
							)
					)
			)).get();

			log.debug(response.getMessage());
		} else {
			BotEnv env = botEnv.orElse(new BotEnv(id));
			Talk talkResponse = talkBot.talk(env.talk().respond(message.getText()));
			env.setContext((String) talkResponse.get("context"));
			env.setMode((String) talkResponse.get("mode"));
			botEnvService.save(env);

			BotApiResponse response = lineMessagingClient.replyMessage(
					new ReplyMessage(
							event.getReplyToken(),
							new TextMessage(talkResponse.getContent())
					)).get();
			log.debug(response.getMessage());
		}
	}

	@EventMapping
	public void handleFallowEvent(FollowEvent event) throws IOException, ExecutionException, InterruptedException {
		lineMessagingClient.replyMessage(
				new ReplyMessage(
						event.getReplyToken(),
						new TextMessage("このBotは docomo Developer supportのAPIを利用しています.\n会話の内容はドコモのサーバに送信されます.")
				)).get();
	}

	@EventMapping
	public void handleJoinEvent(JoinEvent event) throws IOException, ExecutionException, InterruptedException {
		lineMessagingClient.replyMessage(
				new ReplyMessage(
						event.getReplyToken(),
						new TextMessage("このBotは docomo Developer supportのAPIを利用しています.\n会話の内容はドコモのサーバに送信されます.")
				)).get();
	}

	@EventMapping
	public void defaultEvent(Event event) {
		log.debug(event);
	}
}
