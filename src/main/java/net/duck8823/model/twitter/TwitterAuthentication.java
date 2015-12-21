package net.duck8823.model.twitter;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by maeda on 2015/12/20.
 */
@Data
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
@Component
public class TwitterAuthentication {

	private AccessToken accessToken;

	private RequestToken requestToken;

	private String referer;
}
