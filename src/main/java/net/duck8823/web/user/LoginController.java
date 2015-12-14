package net.duck8823.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by maeda on 2015/12/12.
 */
@Controller
@Transactional
public class LoginController {

	@RequestMapping("login")
	public String login(){
		return "user/login";
	}

}
