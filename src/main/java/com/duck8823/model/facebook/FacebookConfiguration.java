package com.duck8823.model.facebook;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.auth.AuthorizationFactory;
import facebook4j.conf.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by maeda on 2016/01/21.
 */
@Configuration
public class FacebookConfiguration {

	@Value("${my.oauth.appId}")
	private String appId;

	@Value("${my.oauth.appSecret}")
	private String appSecret;

	@Value("${my.oauth.permission}")
	private String permission;

	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
	@Bean(name = "authentication")
	public FacebookAuthentication authentication() {
		FacebookAuthentication authentication = new FacebookAuthentication();
		authentication.setFacebook(new FacebookFactory().getInstance());
		return authentication;
	}

	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
	@Bean(name = "myAuthentication")
	public FacebookAuthentication mayFacebookAuthentication() {
		FacebookAuthentication authentication = new FacebookAuthentication();
		authentication.setFacebook(facebook());
		return authentication;
	}

	@Bean(name = "myFacebook")
	public Facebook facebook(){
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthAppId(appId);
		builder.setOAuthAppSecret(appSecret);
		builder.setOAuthPermissions(permission);
		return new FacebookFactory().getInstance(AuthorizationFactory.getInstance(builder.build()));
	}


}
