package net.duck8823;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 例外をハンドリングする設定
 * @author maeda
 *
 */
@Configuration
public class ErrorConfiguration {

	public static final String BAD_REQUEST_PAGE = "/400";
	public static final String UNAUTHORIZED_PAGE = "/401";
	public static final String NOT_FOUND_PAGE = "/404";
	public static final String INTERNAL_SERVER_ERROR_PAGE = "/500";
	
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer(){
		return factory -> {
			factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, BAD_REQUEST_PAGE));
			factory.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_PAGE));
			factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, NOT_FOUND_PAGE));
			factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_PAGE));
		};
	}
}
