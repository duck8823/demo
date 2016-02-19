#Error Handling
エラーコードに応じたページを作る.  
###JavaConfig
```java
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
```
###Controller
```java
package com.duck8823.web.error;

import com.duck8823.ErrorConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * エラー用コントローラ
 * @author maeda
 *
 */
@Controller
public class ErrorController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@RequestMapping(ErrorConfiguration.BAD_REQUEST_PAGE)
	public String badRequestPage(){
		return "error/badRequest";
	}
	
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@RequestMapping(ErrorConfiguration.UNAUTHORIZED_PAGE)
	public String unauthorizedPage(){
		return "error/unauthorized";
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@RequestMapping(ErrorConfiguration.NOT_FOUND_PAGE)
	public String notFoundPage(){
		return "error/notFound";
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@RequestMapping(ErrorConfiguration.INTERNAL_SERVER_ERROR_PAGE)
	public String internalServerErrorPage(){
		return "error/internalServerError";
	}

}
```
  
  
###Thymeleaf
対応するHTMLファイルを作成する.