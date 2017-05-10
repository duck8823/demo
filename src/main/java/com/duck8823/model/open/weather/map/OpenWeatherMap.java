package com.duck8823.model.open.weather.map;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.arnx.jsonic.JSON;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * OpenWeatherMap
 * Created by duck8823 on 2017/05/07.
 */
@Log4j
@AllArgsConstructor
public class OpenWeatherMap {

	private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";

	private String apiKey;

	public JSONObject search(double lat, double lon) throws IOException {
		HttpRequest request = new NetHttpTransport().createRequestFactory().buildGetRequest(new GenericUrl(BASE_URL + "?APPID=" + apiKey + "&lat=" + BigDecimal.valueOf(lat).toPlainString() + "&lon=" + BigDecimal.valueOf(lon).toPlainString()));
		return new JSONObject((Map<String, Object>) JSON.decode(request.execute().getContent()));
	}
}
