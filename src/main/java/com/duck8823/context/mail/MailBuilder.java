package com.duck8823.context.mail;

import lombok.Data;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by maeda on 2016/01/17.
 */
@Data
public class MailBuilder {

	private String systemMailAddress;

	private String adminMailAddress;

	public SimpleMailMessage build(Throwable th) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(systemMailAddress);
		message.setTo(adminMailAddress);
		message.setSubject("例外が発生しました.");
		message.setText(th.toString());
		return message;
	}
}
