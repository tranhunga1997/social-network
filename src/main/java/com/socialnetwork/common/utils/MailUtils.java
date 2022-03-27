package com.socialnetwork.common.utils;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * MailUtils: Xử lý các thao tác mail
 * @author Mạnh Hùng
 *
 */
@Component
public class MailUtils {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    
    /**
     * Gửi mail dạng văn bản
     * @param toEmail
     * @param subject
     * @param message
     */
    public void sendTextMail(String toEmail, String subject, String message){
        MailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send((SimpleMailMessage) mailMessage);
    }

    /**
     * Gửi mail dạng html
     * @param toEmail
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendHtmlMail(String toEmail, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }
    
    /**
     * Gửi mail sử dụng html template
     * @param pathTemplate
     * @param attrs (các tham số truyền vào template dưới dạng Map)
     * @return 
     */
    public String readHtmlTemplateFile(String pathTemplate,Map<String, Object> attrs) {
    	final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    	Context context = new Context(null, attrs);
    	context.setVariable("baseUrl", baseUrl);
    	String process = templateEngine.process(pathTemplate, context);
        return process;
    }

}
