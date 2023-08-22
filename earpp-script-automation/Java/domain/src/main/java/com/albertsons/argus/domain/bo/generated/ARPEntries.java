
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
@JsonPropertyOrder({
    "Internet1",
    "Internet2"
})
@Generated("jsonschema2pojo")
public class ARPEntries {

    @JsonProperty("Internet1")
    private List<Internet1> internet1 = null;
    @JsonProperty("Internet2")
    private List<Internet2> internet2 = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Internet1")
    public List<Internet1> getInternet1() {
        return internet1;
    }

    @JsonProperty("Internet1")
    public void setInternet1(List<Internet1> internet1) {
        this.internet1 = internet1;
    }

    @JsonProperty("Internet2")
    public List<Internet2> getInternet2() {
        return internet2;
    }

    @JsonProperty("Internet2")
    public void setInternet2(List<Internet2> internet2) {
        this.internet2 = internet2;
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
