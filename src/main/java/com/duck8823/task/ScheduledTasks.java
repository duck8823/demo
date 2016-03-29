package com.duck8823.task;

import com.duck8823.service.BotTweetService;
import lombok.extern.log4j.Log4j;
import com.duck8823.model.photo.Photo;
import com.duck8823.model.twitter.Tweet;
import com.duck8823.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.thymeleaf.util.DateUtils;
import twitter4j.TwitterException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;


/**
 * Created by maeda on 2015/12/13.
 */
@Log4j
@Component
public class ScheduledTasks {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private BotTweetService botTweetService;

	@Transactional
	@Scheduled(fixedDelay = 10,  zone = "Asia/Tokyo")
	public void removeFavorite() throws TwitterException {
		log.debug("指定tweetのお気に入りを削除します.");
		botTweetService.removeFavorite(714300941789298688L);
	}


	@Transactional
	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Tokyo")
	public void tweet() throws TwitterException {
		log.info("Twitter 自動投稿");
		Photo photo = photoService.random().get();

		String message = "";
		if(photo.getPlace() != null && !StringUtils.isEmpty(photo.getPlace().getName())){
			message += "【撮影場所】" + photo.getPlace().getName() + "\n";
		}
		message += "【撮影日時】" + DateUtils.format(photo.getTakeDate(), "yyyy/MM/dd HH:mm", Locale.getDefault()) + "\n";

		Tweet tweet = new Tweet(message, photo);
		tweet.touch();
		botTweetService.post(tweet);
	}

	@Transactional
	@Scheduled(cron = "0 */5 * * * *", zone = "Asia/Tokyo")
	public void retweet() throws TwitterException, IOException, URISyntaxException {
		log.debug("フォロワーの水族館写真をリツイート");
		botTweetService.retweet();
	}

	@Transactional
	@Scheduled(cron = "0 */15 * * * *", zone = "Asia/Tokyo")
	public void favorite() throws TwitterException {
		log.debug("水族館の写真検索");
		botTweetService.favorite();
	}

	@Transactional
	@Scheduled(cron = "0 */15 * * * *", zone = "Asia/Tokyo")
	public void followBack() throws TwitterException {
		log.debug("フォロー / フォロー解除");
		botTweetService.followBack();
		botTweetService.unfollow();
	}
}
