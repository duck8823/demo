package com.duck8823.web.servlet.i18n;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 日英切り替え用 LocaleResolver
 * Created by maeda on 2016/01/13.
 */
public class SessionLocaleResolver implements LocaleContextResolver {

	public static final String LOCALE_SESSION_ATTRIBUTE_NAME = SessionLocaleResolver.class.getName() + ".LOCALE";

	@Override
	public LocaleContext resolveLocaleContext(HttpServletRequest request) {
		return new SimpleLocaleContext(resolveLocale(request));
	}

	@Override
	public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
		Locale locale = null;
		if(localeContext != null) {
			locale = localeContext.getLocale();
		}
		WebUtils.setSessionAttribute(request, LOCALE_SESSION_ATTRIBUTE_NAME, locale);
	}

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = (Locale)WebUtils.getSessionAttribute(request, LOCALE_SESSION_ATTRIBUTE_NAME);
		if(locale != null) {
			return locale;
		}
		return selectLocale(request.getLocale());
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		this.setLocaleContext(request, response, locale != null?new SimpleLocaleContext(selectLocale(locale)):null);
	}

	protected Locale selectLocale(Locale locale){
		if(locale == null){
			return null;
		}
		if(locale.getLanguage().equals(Locale.JAPANESE.getLanguage())){
			return Locale.JAPANESE;
		} else {
			return Locale.ENGLISH;
		}
	}
}
