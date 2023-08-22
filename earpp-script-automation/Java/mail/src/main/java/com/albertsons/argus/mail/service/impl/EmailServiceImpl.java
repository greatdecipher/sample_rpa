package com.albertsons.argus.mail.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;

/**
 * 
 * @author kbuen03
 * @version 1.0
 * @since 5/14
 */
@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger LOG = LogManager.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMessageWithAttachment(String from, String aliasFrom, String[] to, String[] cc, String subject, String text,
            String pathToAttachment, Integer priority, boolean isHTML) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress(from, aliasFrom));
            helper.setTo(to);
            if (cc!=null && cc.length > 0 && cc[0].length()>0)
                helper.setCc(cc);

            helper.setPriority(priority);
            helper.setSubject(subject);
            helper.setText(String.format(text), isHTML);

            FileSystemResource file = new FileSystemResource(pathToAttachment);
            helper.addAttachment(file.getFilename(), file);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailParseException(e);
        }
        javaMailSender.send(message);

    }

    @Override
    public void sendSimpleMessage(String from, String aliasFrom, String[] to, String[] cc, String subject, String text,
            Integer priority, boolean isHTML) throws ArgusMailException {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {
            mailMessage.setSubject(subject, "UTF-8");

            MimeMessageHelper message = new MimeMessageHelper(mailMessage, true, "UTF-8");

            message.setFrom(new InternetAddress(from, aliasFrom));
            message.setTo(to);
            
            if (cc!=null && cc.length > 0)
                message.setCc(cc);

            message.setPriority(priority);
            message.setText(text, isHTML);
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOG.error(e);
        }
        try{
            javaMailSender.send(mailMessage);
        }catch(MailException me){
            throw new ArgusMailException("Message failed. ",me);
        }
    }

    @Override
    public void sendMessageWithInlineImage(String from, String aliasFrom, String[] to, String[] cc, String[] bcc,
            String subject, String msg, String imageFile, String imageCID) throws ArgusMailException {
        
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {
            mailMessage.setFrom(from);
            mailMessage.setSubject(subject, "UTF-8");

            for(String toString: to){
                Address toAddress = new InternetAddress(toString);
                mailMessage.addRecipient(Message.RecipientType.TO, toAddress);
            }

            if (cc!=null && cc.length > 0 && cc[0].length()>0) {
                for(String ccString: cc){
                    Address ccAddress = new InternetAddress(ccString);
                    mailMessage.addRecipient(Message.RecipientType.CC, ccAddress);
                }
            }

            if (bcc!=null && bcc.length > 0 && bcc[0].length()>0) {
                for(String bccString: bcc){
                    Address bccAddress = new InternetAddress(bccString);
                    mailMessage.addRecipient(Message.RecipientType.BCC, bccAddress);
                }
            }

            MimeMultipart multipart = new MimeMultipart("related");

            MimeBodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setContent(msg, "text/html");

            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart imageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(new File(imageFile));

            imageBodyPart.setDataHandler(new DataHandler(fds));
            imageBodyPart.setHeader("Content-ID", imageCID);
            
            multipart.addBodyPart(imageBodyPart);

            mailMessage.setContent(multipart);
        } catch (MessagingException e) {
            LOG.error(e);
        }
        try{
            javaMailSender.send(mailMessage);
        }catch(MailException me){
            throw new ArgusMailException("Message failed. ",me);
        }
        
    }

    @Override
    public void sendMessageWithMultipleAttachment(String from, String aliasFrom, String[] to, String[] cc,
            String subject, String text, String pathToAttachment, List<String> fileNames, Integer priority,
            boolean isHTML) throws ArgusMailException{
                MimeMessage message = javaMailSender.createMimeMessage();
               
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setFrom(new InternetAddress(from, aliasFrom));
                    helper.setTo(to);
                    if (cc!=null && cc.length > 0 && cc[0].length()>0)
                        helper.setCc(cc);
        
                    helper.setPriority(priority);
                    helper.setSubject(subject);
                    helper.setText(text, isHTML);
            
                    fileNames.stream().forEach(fileName ->{
                        String fileNameDirectory = pathToAttachment+"//"+fileName;
    
                        LOG.debug("sendMessageWithMultipleAttachment() fileNames: "+fileNameDirectory);
    
                        File file = new File(fileNameDirectory);
                        FileSystemResource fileResource = new FileSystemResource(file);
                        try {
                            helper.addAttachment(file.getName(), fileResource);
                        } catch (MessagingException e) {
                            LOG.error("sendMessageWithMultipleAttachment() exception: "+e);
                        }
                    });
                    
                } catch (MessagingException | UnsupportedEncodingException e) {
                    LOG.error("sendMessageWithMultipleAttachment() exception: "+e);
                    throw new ArgusMailException("Message failed. ",e);
                }
        
                try{
                    javaMailSender.send(message);
                }catch(MailException e){
                    LOG.error("sendMessageWithMultipleAttachment() exception: "+e);
                    throw new ArgusMailException("Message failed. ",e);
                }
    }
}
