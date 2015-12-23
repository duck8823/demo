package net.duck8823.task;

import lombok.extern.log4j.Log4j;
import net.duck8823.model.twitter.Tweet;
import net.duck8823.service.BotTweetService;
import net.duck8823.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.Twitter;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	public void tweet() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		String date = sdf.format(new Date(System.currentTimeMillis()));

		log.info("Twitter 自動投稿 : " + date);
		Tweet tweet = new Tweet("", photoService.random().get());
		tweet.touch();
		botTweetService.post(tweet);
	}
}
