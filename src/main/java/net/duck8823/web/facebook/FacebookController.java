package net.duck8823.web.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import lombok.extern.log4j.Log4j;
import net.duck8823.context.word.TokenizedWordCount;
import net.duck8823.model.facebook.FacebookAuthentication;
import net.duck8823.service.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by maeda on 2015/12/23.
 */
@Log4j
@RequestMapping("facebook")
@Controller
public class FacebookController {

	@Autowired
	private FacebookAuthentication authentication;

	@Autowired
	private FacebookService facebookService;


	@RequestMapping("login")
	public String login(UriComponentsBuilder uriComponentsBuilder) {
		Facebook facebook = new FacebookFactory().getInstance();
		authentication.setFacebook(facebook);
		String callbackURL = uriComponentsBuilder.path("/facebook/callback").build().toString();
		return "redirect:" + facebook.getOAuthAuthorizationURL(callbackURL);
	}

	@RequestMapping("callback")
	public String callback(@RequestParam("code") String oauthCode) throws FacebookException {
		Facebook facebook = authentication.getFacebook();
		facebook.getOAuthAccessToken(oauthCode);
		authentication.setLogin(true);
		return "redirect:" + authentication.getReferer();
	}

	@RequestMapping("wordCloud")
	public String wordCloud() {
		if(!authentication.isLogin()){
			authentication.setReferer("wordCloud");
			return "redirect:login";
		}
		return "facebook/wordCloud";
	}

	@ResponseBody
	@RequestMapping("wordCount")
	public List<TokenizedWordCount> wordCount() throws FacebookException {
		return facebookService.getTokenizedWordCountMap(authentication.getFacebook());
	}

	@RequestMapping("gallery")
	public String gallery(Model model) throws FacebookException {
		if(!authentication.isLogin()){
			authentication.setReferer("gallery");
			return "redirect:login";
		}
		model.addAttribute("medias", facebookService.getPhotos(authentication.getFacebook()));
		return "facebook/gallery";
	}
}
