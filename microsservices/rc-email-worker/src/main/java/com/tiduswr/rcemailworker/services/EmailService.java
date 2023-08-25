package com.tiduswr.rcemailworker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tiduswr.rcemailworker.model.Email;
import com.tiduswr.rcemailworker.model.EmailStatus;
import com.tiduswr.rcemailworker.model.EmailTemplateType;
import com.tiduswr.rcemailworker.model.dto.EmailDTO;
import com.tiduswr.rcemailworker.repositories.EmailRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EmailRepository emailRepository;

    @Transactional(readOnly = false)
    public void sendEmail(EmailDTO email) {
        try {
            Context context = new Context();
            context.setVariable("email", email);

            String content = templateEngine.process(email.emailTemplateType().getTemplateName(), context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(email.emailFrom());
            helper.setTo(email.emailTo());
            helper.setSubject(email.emailSubject());
            helper.setText(content, true);

            javaMailSender.send(message);

            Email pm = parseDtoToEmail(email);
            pm.setEmailStatus(EmailStatus.SENT);
            emailRepository.save(pm);

        } catch (MailException | MessagingException ex) {
            Email pm = parseDtoToEmail(email);
            pm.setEmailStatus(EmailStatus.ERROR);
            emailRepository.save(pm);
        }
    }

    private Email parseDtoToEmail(EmailDTO email){
        Email pm = new Email();

        pm.setId(null);
        pm.setEmailFrom(email.emailFrom());
        pm.setOwnerId(email.ownerId());
        pm.setEmailSubject(email.emailSubject());
        pm.setEmailText(email.emailText());
        pm.setEmailTo(email.emailTo());

        return pm;
    }

}
