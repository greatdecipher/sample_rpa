package com.albertsons.argus.wic.ws.bo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author damis01
 * @since 5/1/22
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "taskList",
})
@Setter
@Getter
@NoArgsConstructor
public class ResponseGetTaskListBO {
    @JsonProperty("taskList")
    private List<TaskBO> taskBo;
}