package net.duck8823.web.photo;

import net.duck8823.model.photo.Photos;
import net.duck8823.model.photo.UploadFiles;
import net.duck8823.model.twitter.Tweet;
import net.duck8823.service.BotTweetService;
import net.duck8823.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import twitter4j.TwitterException;

import java.io.IOException;

/**
 * Created by maeda on 2015/12/12.
 */
@Transactional
@RequestMapping("upload")
@Controller
public class UploadController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private BotTweetService botTweetService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(){
		return "photo/upload";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String upload(UploadFiles files) throws TwitterException {
		Photos photos = new Photos(files);
		photoService.save(new Photos(files));

		Tweet tweet = new Tweet(photos.size() + " 枚の写真をアップロードしました。");
		tweet.touch();
		botTweetService.post(tweet);
		return "photo/upload";
	}

}
