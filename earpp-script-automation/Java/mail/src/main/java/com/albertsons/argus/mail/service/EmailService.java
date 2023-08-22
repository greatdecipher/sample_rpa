package com.albertsons.argus.mail.service;

import java.util.List;

import com.albertsons.argus.mail.exception.ArgusMailException;

/**
 * 
 * @author kbuen03
 * @version 1.0
 * @since 5/14
 */
public interface EmailService {
	public void sendSimpleMessage(String from, String aliasFrom,
			String[] to, String[] cc, String subject, String text, Integer priority, boolean isHTML)
			throws ArgusMailException;

	public void sendMessageWithAttachment(String from, String aliasFrom,
			String[] to, String[] cc, String subject, String text, String pathToAttachment, Integer priority,
			boolean isHTML);

	public void sendMessageWithInlineImage(String from, String aliasFrom, String[] to, String[] cc, String[] bcc,
			String subject, String msg, String imageFile, String imageCID) throws ArgusMailException;

	public void sendMessageWithMultipleAttachment(String from, String aliasFrom,
			String[] to, String[] cc, String subject, String text, String pathToAttachment, List<String> fileNames,
			Integer priority, boolean isHTML)throws ArgusMailException;

}
