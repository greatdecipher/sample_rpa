package com.albertsons.argus.domain.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * @author kbuen03
 * @version 1.1
 * @since 5/18/21
 * 
 * @implNote
 *      - 1.1 - kbuen03 - 1/18/22 - add toJsonString method for convert from obj to string json value
 *      - 1.0 - kbuen03 - 5/18/21 - initial draft
 * 
 */
public interface AutomationService<T> {
    static final Logger LOG = LogManager.getLogger(AutomationService.class);
    abstract Class<T> getTClass();

    public default T toJson(String val){
        LOG.log(Level.DEBUG, () -> "start method . . .");

        ObjectMapper mapper = new ObjectMapper();
        try {
            T t = mapper.readValue(val, getTClass());
            return t;
        } catch (JsonProcessingException e) {
            LOG.log(Level.ERROR, () -> e.getMessage());
        } 
        
        LOG.log(Level.DEBUG, () -> "end method. . .");
        return null;
    }

    public default List<T> getToJsonLists(List<String> listsContent){
        LOG.log(Level.DEBUG, () -> "start method . . .");

        List<T> contentLists = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for(String content : listsContent){
            try {
                T t = mapper.readValue(content, getTClass());
    
                contentLists.add(t);
            } catch (JsonProcessingException e) {
                 LOG.log(Level.ERROR, () -> "getToJsonLists error: "+e.getMessage());
            } 
        }
        LOG.log(Level.DEBUG, () -> "end method. . .");

        return contentLists;
    }

    public default String toJsonString(T obj){
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOG.log(Level.ERROR, () -> "toJsonValue error: "+e.getMessage());
            
            return "";
        }
    }
}
