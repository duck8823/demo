package com.duck8823.web.admin;

import com.duck8823.model.facebook.FacebookAuthentication;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by maeda on 2016/01/23.
 */
@RequestMapping("admin/facebook")
@Controller
public class FacebookAuthController {

	@Qualifier("myAuthentication")
	@Autowired
	private FacebookAuthentication authentication;


	@RequestMapping("auth")
	public String auth(UriComponentsBuilder uriComponentsBuilder) {
		String callbackURL = uriComponentsBuilder.path("admin/facebook/callback").build().toString();
		return "redirect:" + authentication.getFacebook().getOAuthAuthorizationURL(callbackURL);
	}

	@RequestMapping("callback")
	public String callback(@RequestParam("code") String oauthCode) throws FacebookException {
		Facebook facebook = authentication.getFacebook();
		facebook.getOAuthAccessToken(oauthCode);
		return "redirect:upload";
	}

	@RequestMapping("logout")
	public String logout(Model model) {
		model.addAttribute("authentication", authentication);
		authentication.logout();
		return "photo/upload :: btn-facebook";
	}
}
