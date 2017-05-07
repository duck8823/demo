package com.duck8823.model.open.weather.map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenWeatherMapを使う
 * Created by duck8823 on 2017/05/07.
 */
@Configuration
public class OpenWeatherMapConfiguration {

	@Value("${open.weather.map.key}")
	private String key;

	@Bean
	public OpenWeatherMap openWeatherMap() {
		return new OpenWeatherMap(key);
	}
}
