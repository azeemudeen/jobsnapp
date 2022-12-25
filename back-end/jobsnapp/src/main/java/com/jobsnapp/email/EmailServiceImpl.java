package com.jobsnapp.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceImpl implements EmailService{

	@Autowired
	private JavaMailSender emailSender;

	@Override
	public void sendMail(String toEmail, String subject, String compose) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail); 
		message.setSubject(subject); 
		message.setText(compose);
		emailSender.send(message);
	}
}
