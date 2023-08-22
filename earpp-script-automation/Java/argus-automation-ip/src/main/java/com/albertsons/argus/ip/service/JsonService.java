package com.albertsons.argus.ip.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface JsonService<T> {
    static final Logger LOG = LogManager.getLogger(JsonService.class);
    abstract Class<T> getTClass();

    public default T toJson(String val){
        LOG.log(Level.DEBUG, () -> "start method . . .");

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(val, getTClass());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
                e.printStackTrace();
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