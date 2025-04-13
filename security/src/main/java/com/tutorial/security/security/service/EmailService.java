package com.tutorial.security.security.service;

import com.tutorial.security.security.dto.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async("asyncMailSender")
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        String templateName;
        if (emailTemplateName == null)
            templateName = "confirm-email";
        else
            templateName = emailTemplateName.getName();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper( //TODO: handle exceptions
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username",username);
        properties.put("confirmationUrl",confirmationUrl);
        properties.put("activationCode",activationCode);

        //must be from thymeleaf to be able to pass properties to template html
        Context context = new Context();
        context.setVariables(properties);

        messageHelper.setFrom("contact@prospectmanager.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);

        String template = templateEngine.process(templateName, context); //template name & the parameters

        messageHelper.setText(template, true); //mails are html

        mailSender.send(mimeMessage);

    }

}
