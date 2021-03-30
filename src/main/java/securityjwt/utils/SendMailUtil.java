package securityjwt.utils;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SendMailUtil {
	
	@Autowired
	 private static JavaMailSender mailSender;

	public static void sendEmail(String recipientEmail, String link)
	        throws MessagingException, UnsupportedEncodingException {
	    MimeMessage message = mailSender.createMimeMessage();              
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom("sv18600207@gmail.com", "Spring Boot");
	    helper.setTo(recipientEmail);
	     
	    String subject = "Here's the link to reset your password";
	     System.out.println("link: "+link);
	     StringBuilder content = new StringBuilder("<p>Hello,</p>");
	     content.append("<p>You have requested to reset your password.</p>");
	     content.append("<p>Click the link below to change your password:</p>");
	     content.append("<p><b><a href=\"" + link + "\">Change my password</a></b></p>");
	     content.append("<br>");
	     content.append("<p>Ignore this email if you do remember your password, ");
	     content.append("or you have not made the request.</p>");
	     
//	    String content = "<p>Hello,</p>"
//	            + "<p>You have requested to reset your password.</p>"
//	            + "<p>Click the link below to change your password:</p>"
//	            + "<p><b><a href=\"" + link + "\"">Change my password</a></b></p>"
//	            + "<br>"
//	            + "<p>Ignore this email if you do remember your password, "
//	            + "or you have not made the request.</p>";
//	     
	    helper.setSubject(subject);
	     
	    helper.setText(content.toString(), true);
	     
	    mailSender.send(message);
	}
}
