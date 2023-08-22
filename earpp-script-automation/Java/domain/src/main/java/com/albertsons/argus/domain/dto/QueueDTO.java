package com.albertsons.argus.domain.dto;
/**
 * @author kbuen03
 * @since 07/02/2021
 * @version 1.0
 */
public class QueueDTO {
    
    private Integer currentQueueDepth;

    private Integer maxQueueDepth;

    private Integer reader;

    private Integer writer;

    private String queueName;

    private String connection;

    public Integer getCurrentQueueDepth() {
        return currentQueueDepth;
    }

    public void setCurrentQueueDepth(Integer currentQueueDepth) {
        this.currentQueueDepth = currentQueueDepth;
    }

    public Integer getMaxQueueDepth() {
        return maxQueueDepth;
    }

    public void setMaxQueueDepth(Integer maxQueueDepth) {
        this.maxQueueDepth = maxQueueDepth;
    }

    public Integer getReader() {
        return reader;
    }

    public void setReader(Integer reader) {
        this.reader = reader;
    }

    public Integer getWriter() {
        return writer;
    }

    public void setWriter(Integer writer) {
        this.writer = writer;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    
}
