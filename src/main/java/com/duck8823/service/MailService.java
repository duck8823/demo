package com.duck8823.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Created by maeda on 2016/01/17.
 */
@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendMail(SimpleMailMessage message) {
		try {
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
