package com.duck8823.model.photo;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maeda on 2016/01/23.
 */
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_SESSION)
@Component
public class Places extends ArrayList<Place> {

	public Places(List<Place> places){
		super();
		places.stream().forEach(this::add);
	}
}
