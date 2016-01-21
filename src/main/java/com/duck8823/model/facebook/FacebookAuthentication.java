package com.duck8823.model.facebook;

import facebook4j.Facebook;
import lombok.Data;

/**
 * Created by maeda on 2015/12/23.
 */
@Data
public class FacebookAuthentication {

	private Facebook facebook;

	private String referer;

	public boolean isLogin(){
		return facebook.getAuthorization() != null && facebook.getAuthorization().isEnabled();
	}

	public void logout() {
		facebook.setOAuthAccessToken(null);
	}
}
