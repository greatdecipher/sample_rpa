package com.albertsons.argus.domain.bo.generated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "identifier", "label", "timestamp", "items", "rc", "msg" })
@Generated("jsonschema2pojo")
public class ByQueueBO {

    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("label")
    private String label;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("items")
    private List<TmaItem> items = null;
    @JsonProperty("rc")
    private Integer rc;
    @JsonProperty("msg")
    private String msg;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("items")
    public List<TmaItem> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<TmaItem> items) {
        this.items = items;
    }

    @JsonProperty("rc")
    public Integer getRc() {
        return rc;
    }

    @JsonProperty("rc")
    public void setRc(Integer rc) {
        this.rc = rc;
    }

    @JsonProperty("msg")
    public String getMsg() {
        return msg;
    }

    @JsonProperty("msg")
    public void setMsg(String msg) {
        this.msg = msg;
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
