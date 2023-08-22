package com.albertsons.argus.meraki.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * 
 * @author kbuen03
 * @since 04/06/22
 * @version 1.0
 * 
 */
@CrossOrigin
@Api
public interface MerakiAPI {
    
    @PostMapping("/meraki")
	@ApiOperation(value = "To get Meraki ARP JSON", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE )
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully updated"),
	        @ApiResponse(code = 401, message = "Not authorized"),
	        @ApiResponse(code = 403, message = "Forbidden"),
	        @ApiResponse(code = 404, message = "Resource not found"),
	        @ApiResponse(code = 500, message = "Server error")
	})
    public ResponseEntity<Object> getARPRequest(@RequestHeader(value = "Token") String token,
            @RequestBody String merakiArpRequest);
}
