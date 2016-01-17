package com.duck8823.util;

import org.springframework.mail.SimpleMailMessage;

/**
 * Created by maeda on 2016/01/17.
 */
public class MailBuilder {

	private static MailBuilder MAIL_BUILDER = new MailBuilder();
	private MailBuilder() {
	}
	public static MailBuilder getInstance() {
		return MAIL_BUILDER;
	}

	public SimpleMailMessage build(Throwable th) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("system@duck8823.com");
		message.setTo("duck8823@gmail.com");
		message.setSubject("例外が発生しました.");
		message.setText(th.toString());
		return message;
	}
}
