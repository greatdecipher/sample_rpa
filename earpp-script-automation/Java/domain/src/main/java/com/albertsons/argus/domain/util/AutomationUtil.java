package com.albertsons.argus.domain.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.albertsons.argus.domain.bo.generated.ARPEntries;
import com.albertsons.argus.domain.bo.generated.Internet1;
import com.albertsons.argus.domain.bo.generated.Internet2;
import com.albertsons.argus.domain.dto.QueueDTO;

/**
 * @author kbuen03
 * @version 1.0
 * @since 5/18/21
 * @implNote
 *           - 1/20/22 - kbuen03 - add toMerakiOutputHtmlString methdo
 *           - 5/18/21 - kbuen03 - initial draft
 * 
 */
public class AutomationUtil {
    private static final Logger LOG = LogManager.getLogger(AutomationUtil.class);

    private static final String HTML_TD_Red = "<td class=\"tg-hmp3\"><strong style=\"color: red;\">";
    private static final String HTML_TD_STRONG = "</strong></td>";
    private static final String HTML_TD_CLOSE = "</td>";
    private static final String HTML_TD = "<td>";

    public String toTmaHtmlString(List<String> headers, List<QueueDTO> tmaBOs, Integer setQueueDepth,
            boolean isViewRow) {
        LOG.log(Level.DEBUG, () -> "start method . . .");

        StringBuilder sb = new StringBuilder();
        sb.append("<style type='text/css'>.tg  {border-collapse:collapse;border-color:#1a3146;border-spacing:0;}.tg "
                + "td{background-color:#EBF5FF;border-color:#1a3146;border-style:solid;border-width:1px;color:#444;  "
                + "font-family:Calibri, sans-serif;font-size:14px;overflow:hidden;padding:10px 5px;word-break:normal;}.tg "
                + "th{background-color:#409cff;border-color:#1a3146;border-style:solid;border-width:1px;color:#fff;  font-family:Calibri, "
                + "sans-serif;font-size:14px;font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}.tg "
                + ".tg-hmp3{background-color:#eaf2fe;text-align:left;vertical-align:top}.tg "
                + ".tg-hmp4{background-color:#FB4F4F;text-align:left;vertical-align:top}.tg "
                + ".tg-qiqm{background-color:#0071d2;text-align:center;vertical-align:top}</style>");

        sb.append("<body><table class=\"tg\"><thead><tr>");

        if (!tmaBOs.isEmpty()) {
            for (String header : headers) {
                sb.append("<th class=\"tg-qiqm\">");
                sb.append(header + "</span></th>");
            }
            sb.append("</tr></thead>");
            sb.append("<tbody>");
            for (QueueDTO obj : tmaBOs) {

                // Check current queue depth will red hightlight if greater than to set minimum
                // of queue depth
                if (obj.getCurrentQueueDepth() > 0 && obj.getCurrentQueueDepth() > setQueueDepth) {
                    sb.append("<tr>");
                    sb.append(HTML_TD_Red + obj.getConnection() + HTML_TD_STRONG);
                    sb.append(HTML_TD_Red + obj.getQueueName() + HTML_TD_STRONG);
                    sb.append(HTML_TD_Red + obj.getCurrentQueueDepth() + HTML_TD_STRONG);
                    sb.append(HTML_TD_Red + obj.getMaxQueueDepth() + HTML_TD_STRONG);
                    sb.append(HTML_TD_Red + obj.getReader() + HTML_TD_STRONG);
                    sb.append(HTML_TD_Red + obj.getWriter() + HTML_TD_STRONG);
                    sb.append("</tr>");
                } else if ((obj.getCurrentQueueDepth() == 0 && isViewRow)
                        || (obj.getCurrentQueueDepth() > 0 && obj.getCurrentQueueDepth() <= setQueueDepth)) {
                    sb.append("<tr>");
                    sb.append(HTML_TD + obj.getConnection() + HTML_TD_CLOSE);
                    sb.append(HTML_TD + obj.getQueueName() + HTML_TD_CLOSE);
                    sb.append(HTML_TD + obj.getCurrentQueueDepth() + HTML_TD_CLOSE);
                    sb.append(HTML_TD + obj.getMaxQueueDepth() + HTML_TD_CLOSE);
                    sb.append(HTML_TD + obj.getReader() + HTML_TD_CLOSE);
                    sb.append(HTML_TD + obj.getWriter() + HTML_TD_CLOSE);
                    sb.append("</tr>");
                }

            }
            sb.append("</tbody>");
            sb.append("</table></body>");
        }
        LOG.log(Level.DEBUG, () -> "end method . . .");
        return sb.toString();
    }

    public String toDateString(Date date, String dateFormat, String timezoneID) {
        String pattern = dateFormat;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        if (StringUtils.isNotBlank(timezoneID)) { // set timezone only when specified, otherwise, keep as default
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezoneID));
        }

        return simpleDateFormat.format(date);
    }

    public Calendar dateToCalendar(Date date, String timezoneID) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (StringUtils.isNotBlank(timezoneID)) { // set timezone only when specified, otherwise, keep as default
            TimeZone tz = TimeZone.getTimeZone(timezoneID);
            calendar.setTimeZone(tz);
        }

        return calendar;
    }

    public Date toDate(String date, String dateFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        return sdf.parse(date);
    }

    public String toMerakiOutputHtmlString(ARPEntries arpEntries, List<String> headers) {
        LOG.log(Level.DEBUG, () -> "start toMerakiOutputHtmlString method . . .");
        StringBuilder sb = new StringBuilder();
        sb.append("<style type='text/css'>.tg  {border-collapse:collapse;border-color:#1a3146;border-spacing:0;}.tg "
                + "td{background-color:#EBF5FF;border-color:#1a3146;border-style:solid;border-width:1px;color:#444;  "
                + "font-family:Calibri, sans-serif;font-size:14px;overflow:hidden;padding:10px 5px;word-break:normal;}.tg "
                + "th{background-color:#409cff;border-color:#1a3146;border-style:solid;border-width:1px;color:#fff;  font-family:Calibri, "
                + "sans-serif;font-size:14px;font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}.tg "
                + ".tg-hmp3{background-color:#eaf2fe;text-align:left;vertical-align:top}.tg "
                + ".tg-hmp4{background-color:#FB4F4F;text-align:left;vertical-align:top}.tg "
                + ".tg-qiqm{background-color:#0071d2;text-align:center;vertical-align:top}</style>");

        sb.append("<body> Internet 1 <br><table class=\"tg\"><thead><tr>");
        for (String header : headers) {
            sb.append("<th class=\"tg-qiqm\">");
            sb.append(header + "</span></th>");
        }
        sb.append("</tr></thead>");
        sb.append("<tbody>");
        for (Internet1 int1 : arpEntries.getInternet1()) {
            sb.append("<tr>");
            sb.append(HTML_TD + int1.getIp() + HTML_TD_CLOSE);
            sb.append(HTML_TD + int1.getMac() + HTML_TD_CLOSE);
            sb.append(HTML_TD + int1.getVlan() + HTML_TD_CLOSE);
            sb.append(HTML_TD + int1.getAgeInSecs() + HTML_TD_CLOSE);
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table><p>" +
                "Internet 2 <br><table class=\"tg\"><thead><tr>");
        for (String header : headers) {
            sb.append("<th class=\"tg-qiqm\">");
            sb.append(header + "</span></th>");
        }
        sb.append("</tr></thead>");
        sb.append("<tbody>");
        for (Internet2 int2 : arpEntries.getInternet2()) {
            sb.append("<tr>");
            sb.append(HTML_TD + int2.getIp() + HTML_TD_CLOSE);
            sb.append(HTML_TD + int2.getMac() + HTML_TD_CLOSE);
            sb.append(HTML_TD + int2.getVlan() + HTML_TD_CLOSE);
            sb.append(HTML_TD + int2.getAgeInSecs() + HTML_TD_CLOSE);
            sb.append("</tr>");
        }
        sb.append("</tbody>");

        sb.append("</body>");
        LOG.log(Level.DEBUG, () -> "toMerakiOutputHtmlString output: " + sb);
        LOG.log(Level.DEBUG, () -> "end toMerakiOutputHtmlString method . . .");
        return sb.toString();
    }

    public List<String> getHeadersList(String[] headerArr) {
        List<String> getHeadersList = Arrays.asList(headerArr);
        return getHeadersList;
    }

    public String generateNmatchSelector(String prop, String value, String index) {
        String val = "";

        if (StringUtils.isNotBlank(prop))
            val = ":nth-match(:" + prop + "('" + value + "')," + index + ")";
        else
            val = ":nth-match(" + value + "," + index + ")";

        return val;
    }

    public List<String> getStringTokenLists(String str, String delim) {
        return Collections.list(new StringTokenizer(str, delim)).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());
    }

    public void createFile(String strMessage, String file) {
        LOG.log(Level.DEBUG, () -> "start createFile method. . .");

        try {
            if (!fileExists(file)) {
                File myObj = new File(file);
                myObj.createNewFile();
            }

            FileWriter fw = new FileWriter(file, true);
            fw.write(strMessage + "\n");
            fw.close();

        } catch (Exception e) {
            LOG.log(Level.INFO, () -> "error creating file. . .");
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end createFile method. . .");
    }

    public void deleteFile(String folder, Integer maxAge, String filename, boolean deleteByAge) {
        LOG.log(Level.DEBUG, () -> "start deleteFile method. . .");

        try {

            if (deleteByAge == true) { // delete files by age
                File folderPath = new File(folder);
                Path path;
                LocalDateTime ldt;
                BasicFileAttributes bfa;

                long dateDiff;

                if (folderPath.isDirectory()) {
                    try {
                        for (File file : folderPath.listFiles()) {
                            path = Paths.get(file.getPath());
                            bfa = Files.readAttributes(path, BasicFileAttributes.class);
                            ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(bfa.creationTime().toMillis()),
                                    TimeZone.getDefault().toZoneId());
                            dateDiff = ChronoUnit.DAYS.between(ldt, LocalDateTime.now());

                            if (dateDiff > maxAge) {
                                LOG.log(Level.DEBUG, () -> "deleting file. . .");
                                file.delete();
                            }
                        }

                    } catch (Exception e) {
                        LOG.log(Level.INFO, () -> "error while deleting files by age. . .");
                        LOG.error(e);
                    }
                }

            } else if (deleteByAge == false) {

                File file = new File(folder + "\\" + filename);

                if (file.exists()) {
                    file.delete();
                }

            }

        } catch (Exception e) {
            LOG.log(Level.INFO, () -> "error deleting file. . .");
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end deleteFile method. . .");
    }

    public boolean fileExists(String filePath) {
        LOG.log(Level.DEBUG, () -> "start fileExists method . . .");

        try {
            File fileName = new File(filePath);

            if (fileName.exists()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            LOG.error(e);

            return false;
        }

    }

    public FileInputStream getFileInputStream(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(
                file);

        return fis;
    }
    /**
     * Get All names for FileName inside in directory excluding the subdirectory files
     * @param pathDirectory
     * @return
     * @throws IOException
     * @throws SecurityException
     */
    public List<String> getAllFileNameInDirectory(String pathDirectory) throws IOException, SecurityException {
        List<String> fileNameList = new ArrayList<>();
        Stream<Path> paths = Files.walk(Paths.get(pathDirectory));
        fileNameList = paths
                        .filter(Files::isRegularFile)
                    .filter(path -> path.getParent().equals(Paths.get(pathDirectory)))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList())
                // .forEach(c -> fileNameList.add(c.getFileName().toString()))
                ;
        paths.close();

        return fileNameList;
    }

    /**
     * Delete all files in a directory
     * 
     * @param directoryPath
     * @throws IOException
     */
    public void deleteAllFileInFolder(String directoryPath) throws IOException {
        Files.walk(Paths.get(directoryPath))
                // File Only, not include delete the folder
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        LOG.info("deleteAllFileInFolder() Deleted files: " + path);
                    } catch (IOException e) {
                        LOG.error("deleteAllFileInFolder() exception" + e);
                    }
                });
    }

    /**
     * Move All Files in the destination Directory
     * 
     * @param sourceDirectory
     * @param destinationDirectory
     * @throws IOException
     */
    public void moveAllFiles(String sourceDirectory, String destinationDirectory) throws IOException {
        Files.walk(Path.of(sourceDirectory))
                .filter(Files::isRegularFile)
                .forEach(sourcePath -> {
                    try {
                        Path destinationPath = Path.of(destinationDirectory, sourcePath.getFileName().toString());
                        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                        LOG.info("Moved file: " + sourcePath + " to " + destinationPath);
                    } catch (IOException e) {
                        LOG.error("moveAllFiles() exception" + e);
                    }
                });

    }

}
