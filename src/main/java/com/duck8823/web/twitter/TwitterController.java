package com.duck8823.web.twitter;

import lombok.extern.log4j.Log4j;
import com.duck8823.context.word.TokenizedWordCount;
import com.duck8823.model.twitter.TwitterAuthentication;
import com.duck8823.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationContext;

import java.util.List;

/**
 * Created by maeda on 2015/12/20.
 */
@Log4j
@RequestMapping("twitter")
@Controller
public class TwitterController {

	@Autowired
	private TwitterAuthentication authentication;

	@Autowired
	private TwitterService twitterService;


	@RequestMapping("requestToken")
	public String requestToken(UriComponentsBuilder uriComponentsBuilder) throws TwitterException {
		OAuthAuthorization oauth = new OAuthAuthorization(ConfigurationContext.getInstance());
		String callbackURL = uriComponentsBuilder.path("/twitter/accessToken").build().toString();
		RequestToken requestToken = oauth.getOAuthRequestToken(callbackURL);
		authentication.setRequestToken(requestToken);
		return "redirect:" + requestToken.getAuthenticationURL();
	}

	@RequestMapping("accessToken")
	public String accessToken(@RequestParam("oauth_verifier") String verifier) throws TwitterException {
		RequestToken requestToken = authentication.getRequestToken();
		AccessToken accessToken = new AccessToken(requestToken.getToken(), requestToken.getTokenSecret());

		OAuthAuthorization oauth = new OAuthAuthorization(ConfigurationContext.getInstance());
		oauth.setOAuthAccessToken(accessToken);

		accessToken = oauth.getOAuthAccessToken(verifier);
		authentication.setAccessToken(accessToken);

		return "redirect:" + authentication.getReferer();
	}

	@RequestMapping("wordCloud")
	public String wordCloud() {
		if(authentication.getAccessToken() == null){
			authentication.setReferer("wordCloud");
			return "redirect:requestToken";
		}
		return "twitter/wordCloud";
	}

	@ResponseBody
	@RequestMapping("wordCount")
	public List<TokenizedWordCount> wordCount() throws TwitterException {
		Twitter twitter = new TwitterFactory().getInstance(authentication.getAccessToken());
		return twitterService.getTokenizedWordCountMap(twitter);
	}

	@RequestMapping("gallery")
	public String gallery(Model model) throws TwitterException {
		if(authentication.getAccessToken() == null){
			authentication.setReferer("gallery");
			return "redirect:requestToken";
		}
		Twitter twitter = new TwitterFactory().getInstance(authentication.getAccessToken());
		model.addAttribute("medias", twitterService.getTweetMedias(twitter));
		return "twitter/gallery";
	}
}
