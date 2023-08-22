package com.albertsons.argus.domain.bo.generated;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "CurrentQDepth", "MaxQDepth", "OpenInputCount", "OpenOutputCount", "QName", "QType", "Usage",
        "WMQConnection", "id", "CommandLevel", "Platform", "dlq" })
@Generated("jsonschema2pojo")
public class TmaItem {

    @JsonProperty("CurrentQDepth")
    private Integer currentQDepth;
    @JsonProperty("MaxQDepth")
    private Integer maxQDepth;
    @JsonProperty("OpenInputCount")
    private Integer openInputCount;
    @JsonProperty("OpenOutputCount")
    private Integer openOutputCount;
    @JsonProperty("QName")
    private String qName;
    @JsonProperty("QType")
    private Integer qType;
    @JsonProperty("Usage")
    private Integer usage;
    @JsonProperty("WMQConnection")
    private String wMQConnection;
    @JsonProperty("id")
    private String id;
    @JsonProperty("CommandLevel")
    private Integer commandLevel;
    @JsonProperty("Platform")
    private Integer platform;
    @JsonProperty("dlq")
    private Boolean dlq;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("CurrentQDepth")
    public Integer getCurrentQDepth() {
        return currentQDepth;
    }

    @JsonProperty("CurrentQDepth")
    public void setCurrentQDepth(Integer currentQDepth) {
        this.currentQDepth = currentQDepth;
    }

    @JsonProperty("MaxQDepth")
    public Integer getMaxQDepth() {
        return maxQDepth;
    }

    @JsonProperty("MaxQDepth")
    public void setMaxQDepth(Integer maxQDepth) {
        this.maxQDepth = maxQDepth;
    }

    @JsonProperty("OpenInputCount")
    public Integer getOpenInputCount() {
        return openInputCount;
    }

    @JsonProperty("OpenInputCount")
    public void setOpenInputCount(Integer openInputCount) {
        this.openInputCount = openInputCount;
    }

    @JsonProperty("OpenOutputCount")
    public Integer getOpenOutputCount() {
        return openOutputCount;
    }

    @JsonProperty("OpenOutputCount")
    public void setOpenOutputCount(Integer openOutputCount) {
        this.openOutputCount = openOutputCount;
    }

    @JsonProperty("QName")
    public String getQName() {
        return qName;
    }

    @JsonProperty("QName")
    public void setQName(String qName) {
        this.qName = qName;
    }

    @JsonProperty("QType")
    public Integer getQType() {
        return qType;
    }

    @JsonProperty("QType")
    public void setQType(Integer qType) {
        this.qType = qType;
    }

    @JsonProperty("Usage")
    public Integer getUsage() {
        return usage;
    }

    @JsonProperty("Usage")
    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    @JsonProperty("WMQConnection")
    public String getWMQConnection() {
        return wMQConnection;
    }

    @JsonProperty("WMQConnection")
    public void setWMQConnection(String wMQConnection) {
        this.wMQConnection = wMQConnection;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("CommandLevel")
    public Integer getCommandLevel() {
        return commandLevel;
    }

    @JsonProperty("CommandLevel")
    public void setCommandLevel(Integer commandLevel) {
        this.commandLevel = commandLevel;
    }

    @JsonProperty("Platform")
    public Integer getPlatform() {
        return platform;
    }

    @JsonProperty("Platform")
    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    @JsonProperty("dlq")
    public Boolean getDlq() {
        return dlq;
    }

    @JsonProperty("dlq")
    public void setDlq(Boolean dlq) {
        this.dlq = dlq;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
