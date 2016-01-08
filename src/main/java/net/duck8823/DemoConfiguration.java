package net.duck8823;

import net.duck8823.model.photo.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.sql.DataSource;
import java.util.Locale;

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
		sessionFactory.setAnnotatedClasses(Photo.class);
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

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}


	/**
	 * 言語切り替え用インターセプタの設定
	 * @return
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean
	public SessionLocaleResolver sessionLocaleResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(Locale.getDefault());
		return new SessionLocaleResolver();
	}

}
