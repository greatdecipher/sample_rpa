
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
    "RequestId",
    "ARPEntries"
})
@Generated("jsonschema2pojo")
public class MerakiBO {

    @JsonProperty("RequestId")
    private String requestId;
    @JsonProperty("ARPEntries")
    private ARPEntries aRPEntries;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("RequestId")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("RequestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonProperty("ARPEntries")
    public ARPEntries getARPEntries() {
        return aRPEntries;
    }

    @JsonProperty("ARPEntries")
    public void setARPEntries(ARPEntries aRPEntries) {
        this.aRPEntries = aRPEntries;
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
