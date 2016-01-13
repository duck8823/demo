package com.duck8823.model.facebook;

import facebook4j.Facebook;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by maeda on 2015/12/23.
 */
@Data
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
@Component
public class FacebookAuthentication {

	private Facebook facebook;

	private String referer;

	private boolean login;

}
