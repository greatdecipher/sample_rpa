
package com.albertsons.argus.domain.bo.generated;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
/**
 * @author kbuen03 
 * @since 05/25/21
 * @version 1.0
 * 
 */
public class TableGenericBO {
    private Map<String, Object> details = new LinkedHashMap<>();

    public Map<String, Object> getDetails() {
        return details;
    }

    @JsonAnySetter
    public void setDetail(String key, Object value) {
        details.put(key, value);
    }

}
