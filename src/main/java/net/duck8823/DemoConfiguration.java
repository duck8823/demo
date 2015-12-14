package net.duck8823;

import net.duck8823.model.photo.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;


import javax.sql.DataSource;

/**
 * アプリケーション共通の設定
 * @author maeda
 *
 */
@EnableScheduling
@Configuration
public class DemoConfiguration {

	@Autowired
	private DataSource dataSource;
	
	/**
	 * Hibernateの利用
	 * @return
	 */
	@Bean
	public LocalSessionFactoryBean sessionFactory(){
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setAnnotatedClasses(new Class<?>[]{Photo.class});
		return sessionFactory;
	}
	
	/**
	 * JSON出力をpretty形式にする
	 * @return
	 */
	@Bean
	public Jackson2ObjectMapperBuilder jsonBuilder(){
		Jackson2ObjectMapperBuilder jsonBuilder = new Jackson2ObjectMapperBuilder();
		jsonBuilder.indentOutput(true);
		return jsonBuilder;
	}
}
