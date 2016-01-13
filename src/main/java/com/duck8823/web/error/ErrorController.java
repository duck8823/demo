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
