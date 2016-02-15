package com.duck8823.model.twitter;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maeda on 2016/01/11.
 */
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
@Component
public class Filters extends ArrayList<Filter> {

	public Filters(List<Filter> filters){
		super();
		filters.stream().forEach(this::add);
	}

	public boolean filter(Status status){
		return this.stream().filter(filter -> filter.filter(status)).count() > 0;
	}
}
