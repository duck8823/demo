package com.duck8823;

import com.duck8823.model.photo.Photo;
import com.duck8823.model.twitter.Filter;
import com.duck8823.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.sql.DataSource;

/**
 * アプリケーション共通の設定
 * @author maeda
 *
 */
@EnableScheduling
@PropertySources({@PropertySource("classpath:application.properties"), @PropertySource("classpath:twitter4j.properties")})
@Configuration
public class DemoConfiguration {

	@Autowired
	private DataSource dataSource;


	/**
	 * Hibernateの利用
	 * @return
	 */
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setAnnotatedClasses(Photo.class, Filter.class);
		return sessionFactory;
	}

	/**
	 * JSON出力をpretty形式にする
	 * @return
	 */
	@Bean
	public Jackson2ObjectMapperBuilder jsonBuilder() {
		Jackson2ObjectMapperBuilder jsonBuilder = new Jackson2ObjectMapperBuilder();
		jsonBuilder.indentOutput(true);
		return jsonBuilder;
	}

	/**
	 * 多言語用設定ファイル
	 * @return
	 */
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	/**
	 * 言語切り替え
	 * @return
	 */
	@Bean
	public MappedInterceptor interceptor(){
		return new MappedInterceptor(new String[]{"/**"}, localeChangeInterceptor());
	}
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}
	@Bean
	public SessionLocaleResolver localeResolver() {
		return new SessionLocaleResolver();
	}

}
