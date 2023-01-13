package com.jobsnapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jobsnapp.email.EmailService;

@RestController
public class TestController {
	
	@Autowired
	EmailService emailService;
	
	@GetMapping("/sendmail/{toemail}")
	public String sendMail(@PathVariable("toemail") String toEmail) {
		if(toEmail == null || !toEmail.contains("@")) {
			return "Invalid Email!";			
		}
		boolean sentSuccess = emailService.sendMail(toEmail, "Test", "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n"
				+ "  border=\"0\">\r\n"
				+ "  <tbody>\r\n"
				+ "    <tr>\r\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\"\r\n"
				+ "        align=\"left\">\r\n"
				+ "        <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n"
				+ "          <tr>\r\n"
				+ "            <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\r\n"
				+ "              <img align=\"center\" border=\"0\" src=\"https://iili.io/HIeK9Nn.md.png\" alt=\"\" title=\"\"\r\n"
				+ "                style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 480px;\"\r\n"
				+ "                width=\"480\" />\r\n"
				+ "            </td>\r\n"
				+ "          </tr>\r\n"
				+ "        </table>\r\n"
				+ "      </td>\r\n"
				+ "    </tr>\r\n"
				+ "    <tr><b>Congratlation [name]!\r\n"
				+ "    <p>Your shorlisted for [job title] by [recruiter_name]\r\n"
				+ "    </b></tr>\r\n"
				+ "  </tbody>\r\n"
				+ "</table>");
		return sentSuccess? "Email Sent!": "Email Failed";
	}
}
