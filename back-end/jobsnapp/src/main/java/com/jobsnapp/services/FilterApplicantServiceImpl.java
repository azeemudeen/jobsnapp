package com.jobsnapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsnapp.email.EmailService;
import com.jobsnapp.model.Job;
import com.jobsnapp.model.User;

@Service
public class FilterApplicantServiceImpl implements FilterApplicantService{
	
	@Autowired
	EmailService emailService;
	
	
	@Override
	public void process(Set<User> userApplied, ArrayList<Long> filteredUserList,Job job) {
		if(userApplied.isEmpty() || filteredUserList.isEmpty()) {
			return;
		}
		Map<Boolean, List<User>> filteredMap  = userApplied.stream().collect(Collectors.partitioningBy(u -> filteredUserList.contains(u.getId())));
		
		List<User> selectedApplicants = filteredMap.get(true);
		List<User> rejectedApplicants = filteredMap.get(false);
		
		selectedApplicants.parallelStream().forEach((user)->{
			String subjectSelected = "Congrats "+user.getName()+", you're filtered!";
			String body = "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n"
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
				+ "    <tr style=\"font-size:medium\"><b>Congratlation "+user.getName()+" "+user.getSurname()+"!\r\n"
				+ "    <p style=\"font-size:medium\">Your shorlisted for the job position: "+job.getTitle()+" posted by "+job.getUserMadeBy().getName()+" , Kindly contact recruiter by email:- "+job.getUserMadeBy().getUsername()+"\r\n"
				+ "    </b></tr>\r\n"
				+ "  </tbody>\r\n"
				+ "</table>";
			emailService.sendMail(user.getUsername(), subjectSelected, body);
		});
		
		rejectedApplicants.parallelStream().forEach((user)->{
			String subjectSelected = "Unfortunatly "+user.getName()+", you're not filtered!";
			String body = "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n"
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
				+ "    <tr style=\"font-size:medium\"><b>Unfortunate news "+user.getName()+" "+user.getSurname()+"!\r\n"
				+ "    <p style=\"font-size:medium\">Your not shorlisted for Job title:"+job.getTitle()+" posted by "+job.getUserMadeBy().getName()+", Never giveup Keep trying!\r\n"
				+ "    </b></tr>\r\n"
				+ "  </tbody>\r\n"
				+ "</table>";
			emailService.sendMail(user.getUsername(), subjectSelected, body);
		});
	}
	
}
