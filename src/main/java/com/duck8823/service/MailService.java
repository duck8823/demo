package com.duck8823.service;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Created by maeda on 2016/01/17.
 */
@Log4j
@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendMail(SimpleMailMessage message) {
		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error("メールの送信に失敗しました.", e);
		}
	}
}
