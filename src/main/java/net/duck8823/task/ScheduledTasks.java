package net.duck8823.task;

import lombok.extern.log4j.Log4j;
import net.duck8823.service.PhotoService;
import net.duck8823.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.io.ByteArrayInputStream;
import java.security.Timestamp;
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

	@Transactional
	@Scheduled(cron = "0 0 * * * *", zone = "Asia/Tokyo")
	public void tweet() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		String date = sdf.format(new Date(System.currentTimeMillis()));

		log.info("Twitter 自動投稿 : " + date);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.updateStatus(new StatusUpdate(date).media("photo", new ByteArrayInputStream(photoService.random().get().getImage())));
	}
}
