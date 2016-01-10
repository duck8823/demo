package net.duck8823.task;

import lombok.extern.log4j.Log4j;
import net.duck8823.model.twitter.Tweet;
import net.duck8823.service.BotTweetService;
import net.duck8823.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.TwitterException;

import java.io.IOException;
import java.net.URISyntaxException;


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
	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Tokyo")
	public void tweet() throws TwitterException {
		log.info("Twitter 自動投稿");
		Tweet tweet = new Tweet("", photoService.random().get());
		tweet.touch();
		botTweetService.post(tweet);
	}

	@Transactional
	@Scheduled(cron = "0 */15 * * * *", zone = "Asia/Tokyo")
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
