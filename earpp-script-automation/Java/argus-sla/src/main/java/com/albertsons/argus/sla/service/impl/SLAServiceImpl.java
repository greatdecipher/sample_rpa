package com.albertsons.argus.sla.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.albertsons.argus.mail.exception.ArgusMailException;
import com.albertsons.argus.mail.service.EmailService;
import com.albertsons.argus.sla.dto.SLAReport;
import com.albertsons.argus.sla.exception.SLAException;
import com.albertsons.argus.sla.service.SLAService;
import com.albertsons.argus.webservice.bo.ResponseEndSaveBO;
import com.albertsons.argus.webservice.bo.ResponseIncrementTransactionBO;
import com.albertsons.argus.webservice.bo.ResponseStartSaveBO;
import com.albertsons.argus.webservice.service.MetricsWebService;

@Service
public class SLAServiceImpl implements SLAService {
	private static final Logger LOG = LogManager.getLogger(SLAServiceImpl.class);

	@Autowired
	private Environment environment;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
    private MetricsWebService metricsWebService;

    private String automationRunId;
	private Integer index = 0;

	@Override
	public boolean checkIfReportExists() {
		LOG.log(Level.INFO, () -> "Start checkIfReportExists method. . .");

		Process vbscript;
		try {
			vbscript = Runtime.getRuntime().exec("wscript " + environment.getProperty("sla.path.to.script") + " "
					+ environment.getProperty("sla.outlook.email.values"));
			vbscript.waitFor(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOG.log(Level.INFO, () -> "Error at reading email: ");
			LOG.error(e);
			return false;
		}

		File file = new File(environment.getProperty("sla.folder.file.path"));
		if (file.exists() && file.isFile()) {
			LOG.info("Report found . . .");
			return true;
		} else {
			LOG.info("Report not found . . .");
			return false;
		}
	}

	@Override
	public Map<String, List<SLAReport>> readExcel() throws SLAException {
		LOG.info("Start reading excel file . . .");
		Map<String, List<SLAReport>> reportMap = new HashMap<>();
		int column_H = 7;
		FileInputStream inputStream = null;
		try {
			File file = new File(environment.getProperty("sla.folder.file.path"));
			inputStream = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			for (Row row : sheet) {
				List<SLAReport> newreportList = new ArrayList<>();
				SLAReport reportDetails = new SLAReport();
				Cell emailCell = row.getCell(column_H);
				String managersEmail = emailCell.getStringCellValue();

				if (row.getRowNum() == 0) {
					// header
					continue;
				}

				for (Cell cell : row) {
					switch (cell.getColumnIndex()) {
						case 0:
							reportDetails.setNumber(cell.getStringCellValue());
							break;
						case 1:
							reportDetails.setPriority(cell.getStringCellValue());
							break;
						case 2:
							reportDetails.setGroup(cell.getStringCellValue());
							break;
						case 3:
							reportDetails.setState(cell.getStringCellValue());
							break;
						case 4:
							reportDetails.setOpened(cell.getDateCellValue());
							break;
						case 5:
							reportDetails.setDescription(cell.getStringCellValue());
							break;
						case 6:
							reportDetails.setManager(cell.getStringCellValue());
							break;
						case 7:
							reportDetails.setManagerEmail(cell.getStringCellValue());
							break;
						case 8:
							reportDetails.setDirector(cell.getStringCellValue());
							break;
						case 9:
							reportDetails.setDirectorEmail(cell.getStringCellValue());
							break;
						default:
					}
				}

				if ((reportDetails.getGroup() == null || reportDetails.getGroup().isBlank()) ||
					(managersEmail == null || managersEmail.isBlank()) && 
					(reportDetails.getDirectorEmail() == null || reportDetails.getDirectorEmail().isBlank())) {
					managersEmail = "Ignored";
				} else if ((managersEmail == null || managersEmail.isBlank()) && 
							(reportDetails.getDirectorEmail() != null && !reportDetails.getDirectorEmail().isBlank())) {
					managersEmail = reportDetails.getDirectorEmail();
				} else {
					if (index == 0) {
						index = row.getRowNum();
					}
				}

				if (!reportMap.containsKey(managersEmail)) {
					reportMap.put(managersEmail, newreportList);
				}
				reportMap.get(managersEmail).add(reportDetails);
			}
			
			inputStream.close();
			if (workbook != null) {
				workbook.close();
			}
		} catch (Exception e) {
			LOG.error(e);
			LOG.log(Level.INFO, () -> "Error at reading excel file . . . ");
			throw new SLAException(e.getMessage());
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				LOG.error(e);
				LOG.log(Level.INFO, () -> "Error at closing FileInputStream . . . ");
				throw new SLAException(e.getMessage());
			}
		}
		LOG.info("Successful reading excel file . . .");
		return reportMap;
	}

	@Override
	public Integer prepareAndSendReport(Map<String, List<SLAReport>> reportMap) throws SLAException {
		LOG.info("Start preparing report for each manager . . .");
		metricsSLA("START", 0);
		int transactionCnt = 0;
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			File inputFile = new File(environment.getProperty("sla.folder.file.path"));
			inputStream = new FileInputStream(inputFile);
			XSSFWorkbook srcWorkbook = new XSSFWorkbook(inputStream);
			XSSFSheet srcSheet = srcWorkbook.getSheetAt(0);
			XSSFRow srcRowHeader = srcSheet.getRow(0);
			
			try {
				if (srcWorkbook != null) {
					srcWorkbook.close();
				}
			} catch (Exception e) {
				LOG.error(e);
				LOG.log(Level.INFO, () -> "Error at closing input workbook . . . ");
				throw new SLAException(e.getMessage());
			}
			for (String s : reportMap.keySet()) {
				String managersEmail = s;
				List<SLAReport> reportList = reportMap.get(s);
				SLAReport minReport = Collections.min(reportList, Comparator.comparing(sla -> sla.getOpened()));
				Date openedDate = minReport.getOpened();
				String managersName = new String();
				String dateToday = todaysDate();

				String outFilePath = environment.getProperty("sla.folder.file.out.path") + " " + dateToday + ".xlsx";
				if (managersEmail.equals("Ignored")) {
					outFilePath = environment.getProperty("sla.folder.file.fail.path");
				}
				File outputFile = new File(outFilePath);
				outputStream = new FileOutputStream(outputFile);

				XSSFWorkbook workBook = new XSSFWorkbook();
				XSSFSheet sheet = workBook.createSheet(srcSheet.getSheetName());
				XSSFRow srcRow = srcSheet.getRow(index); 

				int rowNum = 1;
				
				// Create header
				XSSFRow rowHeader = sheet.createRow(srcRowHeader.getRowNum());
				for (Cell srcCell : srcRowHeader) {
					XSSFCell cell = rowHeader.createCell(srcCell.getColumnIndex());
					XSSFCellStyle newCellStyle = workBook.createCellStyle();
					if (srcCell.getCellStyle() != null) {
						newCellStyle.cloneStyleFrom(srcCell.getCellStyle());
						cell.setCellStyle(newCellStyle);
					}
					cell.setCellValue(srcCell.getStringCellValue());
				}

				for (SLAReport report : reportList) {
					// Create new row
					XSSFRow row = sheet.createRow(rowNum);

					for (int i = 0; i < rowHeader.getLastCellNum(); i++) {
						// Create cell and copy cellstyle
						XSSFCell srcCell = srcRow.getCell(i);
						XSSFCell cell = row.createCell(i);
						XSSFCellStyle newCellStyle = workBook.createCellStyle();
						if (srcCell != null) {
							newCellStyle.cloneStyleFrom(srcCell.getCellStyle());
							cell.setCellStyle(newCellStyle);
						}
						cell.getRow().setHeight((short) -1);

						// Set value
						switch (cell.getColumnIndex()) {
							case 0:
								cell.setCellValue(report.getNumber());
								break;
							case 1:
								cell.setCellValue(report.getPriority());
								break;
							case 2:
								cell.setCellValue(report.getGroup());
								break;
							case 3:
								cell.setCellValue(report.getState());
								break;
							case 4:
								cell.setCellValue(report.getOpened());
								break;
							case 5:
								cell.setCellValue(report.getDescription());
								break;
							case 6:
								cell.setCellValue(report.getManager());
								break;
							case 7:
								cell.setCellValue(report.getManagerEmail());
								break;
							case 8:
								cell.setCellValue(report.getDirector());
								break;
							case 9:
								cell.setCellValue(report.getDirectorEmail());
								break;
							default:
								break;
						}
						sheet.setColumnWidth(cell.getColumnIndex(), srcSheet.getColumnWidth(i));
					}
					rowNum++;

					if (managersName.isBlank()) {
						managersName = report.getManager();
					}
				}
				sheet.createFreezePane(0, 1);
				sheet.setActiveCell(new CellAddress("A2"));

				try {
					if (workBook != null) {
						workBook.write(outputStream);
						workBook.close();
					}
				} catch (Exception e) {
					LOG.error(e);
					LOG.log(Level.INFO, () -> "Error at closing output workbook . . . ");
					throw new SLAException(e.getMessage());
				} finally {
					outputStream.close();
				}
				LOG.info("Successfully created report  . . .");
				if (!managersEmail.equals("Ignored")) {
					if (environment.getProperty("spring.profiles.active").equalsIgnoreCase("dev") ||
							environment.getProperty("spring.profiles.active").equalsIgnoreCase("qa")) {
						managersEmail = environment.getProperty("sla.mail.handler.recipient");
					}
					sendEmailReport(openedDate, managersName, managersEmail, outFilePath);
					transactionCnt++;
					outputFile.delete();
				}
			}
			inputFile.delete();
			metricsSLA("END", transactionCnt);
		} catch (Exception e) {
			LOG.error(e);
			LOG.log(Level.INFO, () -> "Error at preparing or sending report to manager . . . ");
			throw new SLAException(e.getMessage());
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				LOG.error(e);
				LOG.log(Level.INFO, () -> "Error at closing FileInputStream or FileOutputStream . . . ");
				throw new SLAException(e.getMessage());
			}
		}

		return transactionCnt;
	}

	public void sendEmailReport(Date opened, String managerName, String managerEmail, String filePath)
			throws IOException, SLAException {
		String fromEmail = environment.getProperty("sla.mail.handler.sender");
		String[] toEmail = new String[] { managerEmail };
		String[] cc = environment.getProperty("sla.mail.handler.itsm", String[].class);
		String[] bcc = environment.getProperty("sla.mail.handler.bcc", String[].class);
		String subject = new String();
		String body = new String();
		String day = environment.getProperty("sla.string.dayofweek");
		String mondayEmail = "";

		LocalDate today = LocalDate.now();
		DayOfWeek dayOfWeek = today.getDayOfWeek();

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
			String todaysDate = today.format(formatter);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			String openedStr = sdf.format(opened.getTime());
			subject = environment.getProperty("sla.mail.subject") + " " + todaysDate + " – " + managerName;

			body = "<p style='margin-top:0in;margin-right:0in;margin-bottom:8.0pt;margin-left:0in;line-height:107%;font-size:15px;font-family:&quot;Calibri&quot;,sans-serif;'><span style='font-size:12px;line-height:107%;font-family:&quot;Calibri Light&quot;,sans-serif;'>Hi "
					+ managerName + ", Good day!</span></p>"
					+ "<p style='margin-top:0in;margin-right:0in;margin-bottom:8.0pt;margin-left:0in;line-height:107%;font-size:15px;font-family:&quot;Calibri&quot;,sans-serif;'><span style='font-size:12px;line-height:107%;font-family:&quot;Calibri Light&quot;,sans-serif;'>&nbsp;</span></p>"
					+ "<p style='margin-top:0in;margin-right:0in;margin-bottom:8.0pt;margin-left:0in;line-height:107%;font-size:15px;font-family:&quot;Calibri&quot;,sans-serif;'><span style='font-size:12px;line-height:107%;font-family:&quot;Calibri Light&quot;,sans-serif;'>We would like to make a follow up regarding"
					+ " the active Out of Sla Incidents assigned to your group/s as of " + todaysDate
					+ " with oldest active Incident dated " + openedStr + ".</span></p>";
			body += environment.getProperty("email.body");
			body += environment.getProperty("email.footer");
			body += "<img src=\"cid:image\">";

			if (environment.getProperty("spring.profiles.active").equalsIgnoreCase("prod")
					&& dayOfWeek == DayOfWeek.WEDNESDAY) {
				LocalDate sunday = today.minusDays(3);
				String formattedSunday = sunday.format(formatter);
				String searchSubject = environment.getProperty("sla.mail.subject") + " " + formattedSunday + " – "
						+ managerName;
				mondayEmail = searchAndGetEmail(searchSubject);
			} else if (!environment.getProperty("spring.profiles.active").equalsIgnoreCase("prod")
					&& day.equalsIgnoreCase("WEDNESDAY")) {
					String searchSubject = environment.getProperty("sla.mail.subject") + " " 
					+ environment.getProperty("sla.string.date") + " – " + managerName;
				mondayEmail = searchAndGetEmail(searchSubject);
			}

			if (!mondayEmail.isBlank()) {
				body += mondayEmail;
			}
			sendMessageWithInlineImageAndAttachment(fromEmail, fromEmail, toEmail, cc, bcc,
					subject, body, environment.getProperty("sla.path.to.image"), "<image>", filePath);
		} catch (Exception e) {
			LOG.error(e);
			LOG.log(Level.INFO, () -> "Error at preparing email for managers . . . ");
			throw new SLAException(e.getMessage());
		}

	}

	public String searchAndGetEmail(String searchSubject) {
		int exitValue = 0;
		String emailHTML = "";
		try {
			String command = "cscript " + environment.getProperty("sla.path.to.script.mail") + " \""
					+ searchSubject + "\"";

			Process process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			StringBuilder output = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				output.append(line);
			}

			exitValue = process.waitFor();

			if (exitValue == 0) {
				emailHTML = output.toString();
				int index = emailHTML.indexOf("-----Original Message-----");
				if (index != -1) {
					emailHTML = emailHTML.substring(index);
				}
				emailHTML = "<br><br>" + emailHTML;
				LOG.info("Email found . . .");
			} else {
				LOG.info("Email not found . . .");
			}
		} catch (IOException e) {
			LOG.error(e);
		} catch (InterruptedException e) {
			LOG.error(e);
			LOG.info("An error occured . . .");
		}
		return emailHTML;
	}

	public void sendMessageWithInlineImageAndAttachment(String from, String aliasFrom, String[] to, String[] cc,
			String[] bcc,
			String subject, String msg, String imageFile, String imageCID, String pathToAttachment)
			throws ArgusMailException, IOException {

		MimeMessage mailMessage = javaMailSender.createMimeMessage();
		try {
			mailMessage.setFrom(from);
			mailMessage.setSubject(subject, "UTF-8");

			for (String toString : to) {
				Address toAddress = new InternetAddress(toString);
				mailMessage.addRecipient(Message.RecipientType.TO, toAddress);
			}

			if (cc != null && cc.length > 0 && cc[0].length() > 0) {
				for (String ccString : cc) {
					Address ccAddress = new InternetAddress(ccString);
					mailMessage.addRecipient(Message.RecipientType.CC, ccAddress);
				}
			}

			if (bcc != null && bcc.length > 0 && bcc[0].length() > 0) {
				for (String bccString : bcc) {
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

			MimeBodyPart attachmentPart = new MimeBodyPart();
			attachmentPart.attachFile(pathToAttachment);
			multipart.addBodyPart(attachmentPart);

			mailMessage.setContent(multipart);
		} catch (MessagingException e) {
			LOG.error(e);
		}
		try {
			javaMailSender.send(mailMessage);
			LOG.info("Email succesfully sent . . .");
		} catch (MailException me) {
			LOG.error(me);
			LOG.log(Level.INFO, () -> "Error at sending email . . . ");
			throw new ArgusMailException("Message failed. ", me);
		}

	}

	@Override
	public void sendMailNotification(String notif, int num) {
		String dateToday = todaysDate();
		try {
			if (notif.equals("MISSING_REPORT")) {
				emailService.sendSimpleMessage(environment.getProperty("sla.mail.handler.rpa"),
						environment.getProperty("sla.mail.handler.rpa"),
						environment.getProperty("sla.mail.handler.itsm", String[].class),
						environment.getProperty("sla.mail.missing.report.cc", String[].class),
						environment.getProperty("sla.mail.missing.report.subject") + " " + dateToday,
						environment.getProperty("sla.mail.missing.report.message"),
						1,
						false);
				LOG.info("Missing report notification email sent . . .");
			} if (notif.equals("NO_REPORT")) {
				emailService.sendSimpleMessage(environment.getProperty("sla.mail.handler.rpa"),
						environment.getProperty("sla.mail.handler.rpa"),
						environment.getProperty("sla.mail.handler.itsm", String[].class),
						environment.getProperty("sla.mail.missing.report.cc", String[].class),
						environment.getProperty("sla.mail.no.report.subject") + " " + dateToday,
						environment.getProperty("sla.mail.missing.report.message") + "\n\n" +
						environment.getProperty("sla.mail.no.report.message"),
						1,
						false);
				LOG.info("No processed report notification email sent . . .");
			} else if (notif.equals("START")) {
				emailService.sendSimpleMessage(environment.getProperty("sla.mail.handler.rpa"),
						environment.getProperty("sla.mail.handler.rpa"),
						environment.getProperty("sla.mail.handler.recipient", String[].class),
						environment.getProperty("sla.mail.handler.cc", String[].class),
						environment.getProperty("sla.mail.start.subject") + " " + dateToday,
						environment.getProperty("sla.mail.start.message"),
						3,
						false);
				LOG.info("Start Out of SLA Follow-up process notification email sent. . .");
			} else if (notif.equals("ERROR")) {
				emailService.sendSimpleMessage(environment.getProperty("sla.mail.handler.rpa"),
						environment.getProperty("sla.mail.handler.rpa"),
						environment.getProperty("sla.mail.handler.recipient", String[].class),
						environment.getProperty("sla.mail.handler.cc", String[].class),
						environment.getProperty("sla.mail.error.subject") + " " + dateToday,
						environment.getProperty("sla.mail.error.message"),
						3,
						false);
				LOG.info("Error notification email sent . . .");
			} else if (notif.equals("FINISH")) {
				File file = new File(environment.getProperty("sla.folder.file.fail.path"));
				if (file.exists() && file.isFile()) {
					emailService.sendMessageWithAttachment(environment.getProperty("sla.mail.handler.rpa"),
							environment.getProperty("sla.mail.handler.rpa"),
							environment.getProperty("sla.mail.handler.itsm", String[].class),
							environment.getProperty("sla.mail.handler.cc", String[].class),
							environment.getProperty("sla.mail.finish.subject") + " " + dateToday,
							environment.getProperty("sla.mail.finish.message") + num + "\n\n" +
									environment.getProperty("sla.mail.finish.ignore.message"),
							environment.getProperty("sla.folder.file.fail.path"),
							3,
							false);
					file.delete();
				} else {
					emailService.sendSimpleMessage(environment.getProperty("sla.mail.handler.rpa"),
							environment.getProperty("sla.mail.handler.rpa"),
							environment.getProperty("sla.mail.handler.itsm", String[].class),
							environment.getProperty("sla.mail.handler.cc", String[].class),
							environment.getProperty("sla.mail.finish.subject") + " " + dateToday,
							environment.getProperty("sla.mail.finish.message") + num,
							3,
							false);
				}
				LOG.info("Out of SLA Follow-up process completion email sent . . .");
			}
		} catch (Exception e) {
			LOG.error(e);
			LOG.info("Error encountered in sending email notification . . .");
		}

	}

	public String todaysDate() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		String formattedDate = today.format(formatter);
		return formattedDate;
	}

	public void metricsSLA(String status, Integer ctr) {
		if (status.equals("START")) {
			try {
				ResponseStartSaveBO bo = metricsWebService.startSave("A100144", "1.0");

				if (bo.getResult().contains("SUCCESS")) {
					automationRunId = bo.getId();
				} else {
					LOG.log(Level.DEBUG, () -> "problem on Start Save. . .");
				}

			} catch (Exception e) {
				LOG.error("error on Start Save. . .");
				LOG.error(e);
			}
		} else if (status.equals("END")) {

			try {
				ResponseIncrementTransactionBO bo2 = metricsWebService.incrementTransaction(automationRunId,
						Integer.toString(ctr));

				if (bo2.getResult().contains("SUCCESS")) {
					LOG.log(Level.DEBUG, () -> "Ending save. . .");
					ResponseEndSaveBO bo3 = metricsWebService.endSave(automationRunId);

					if (bo3.getResult().contains("SUCCESS")) {
						LOG.log(Level.DEBUG, () -> "Transaction count successfully saved. . .");
					} else {
						LOG.log(Level.DEBUG, () -> "problem on End Save. . .");
					}
				} else {
					LOG.log(Level.DEBUG, () -> "problem on Increment Transaction. . .");
				}

			} catch (Exception e) {
				LOG.error("error on Increment Transaction or End Save. . .");
				LOG.error(e);
			}
		}
	}
}
