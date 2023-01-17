package com.jobsnapp.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private JavaMailSender emailSender;

	@Override
	public boolean sendMail(String toEmail, String subject, String compose) {
		try{
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);
			messageHelper.setTo(toEmail); 
			messageHelper.setSubject(subject); 
			messageHelper.setText(compose,true);
			emailSender.send(message);
			return true;
		} catch(Exception e){
			System.out.println("Email Failed");
		}
		return false;
	}
}
