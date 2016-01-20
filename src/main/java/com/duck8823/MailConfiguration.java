package com.duck8823;

import com.duck8823.context.mail.MailBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by maeda on 2016/01/17.
 */
@Configuration
public class MailConfiguration {

	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private Integer port;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${app.system.mail.address}")
	private String systemMailAddress;

	@Value("${app.admin.mail.address}")
	private String adminMailAddress;

	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(host);
		sender.setPort(port);
		sender.setUsername(username);
		sender.setPassword(password);
		return sender;
	}

	@Bean
	public MailBuilder mailBuilder() {
		MailBuilder builder = new MailBuilder();
		builder.setSystemMailAddress(systemMailAddress);
		builder.setAdminMailAddress(adminMailAddress);
		return builder;
	}
}
