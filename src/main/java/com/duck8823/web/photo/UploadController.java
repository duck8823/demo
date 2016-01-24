package com.duck8823.web.photo;

import com.duck8823.model.facebook.FacebookAuthentication;
import com.duck8823.model.facebook.Post;
import com.duck8823.model.photo.Photos;
import com.duck8823.model.photo.UploadFiles;
import com.duck8823.service.BotTweetService;
import com.duck8823.model.twitter.Tweet;
import com.duck8823.service.MyFacebookService;
import com.duck8823.service.PhotoService;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import twitter4j.TwitterException;

/**
 * Created by maeda on 2015/12/12.
 */
@Transactional
@RequestMapping("manage/photo/upload")
@Controller
public class UploadController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private BotTweetService botTweetService;

	@Autowired
	private MyFacebookService myFacebookService;

	@Qualifier("myAuthentication")
	@Autowired
	private FacebookAuthentication authentication;

	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model){
		model.addAttribute("authentication", authentication);
		return "photo/upload";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String upload(Model model, UploadFiles files) throws TwitterException, FacebookException {
		model.addAttribute("authentication", authentication);

		Photos photos = new Photos(files);
		photoService.save(photos);

		if(authentication.isLogin()) {
			myFacebookService.post(new Post(photos.size() + " 枚の写真をアップロードしました。\nby http://www.duck8823.com\n", photos));
		}

		Tweet tweet = new Tweet(photos.size() + " 枚の写真をアップロードしました。\nhttp://www.duck8823.com");
		tweet.touch();
		botTweetService.post(tweet);
		return "photo/upload";
	}

}
