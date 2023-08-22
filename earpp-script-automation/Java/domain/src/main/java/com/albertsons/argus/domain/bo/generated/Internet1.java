
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
@JsonPropertyOrder({
    "IP",
    "MAC",
    "VLAN",
    "AgeInSecs"
})
@Generated("jsonschema2pojo")
public class Internet1 {

    @JsonProperty("IP")
    private String ip;
    @JsonProperty("MAC")
    private String mac;
    @JsonProperty("VLAN")
    private Integer vlan;
    @JsonProperty("AgeInSecs")
    private Integer ageInSecs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("IP")
    public String getIp() {
        return ip;
    }

    @JsonProperty("IP")
    public void setIp(String ip) {
        this.ip = ip;
    }

    @JsonProperty("MAC")
    public String getMac() {
        return mac;
    }

    @JsonProperty("MAC")
    public void setMac(String mac) {
        this.mac = mac;
    }

    @JsonProperty("VLAN")
    public Integer getVlan() {
        return vlan;
    }

    @JsonProperty("VLAN")
    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    @JsonProperty("AgeInSecs")
    public Integer getAgeInSecs() {
        return ageInSecs;
    }

    @JsonProperty("AgeInSecs")
    public void setAgeInSecs(Integer ageInSecs) {
        this.ageInSecs = ageInSecs;
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
