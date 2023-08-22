package com.albertsons.argus.webservice.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * 
 * @author jborj20
 * @since 11/10/22
 * @version 1.0
 */
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {

        return (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                ||
                response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            // Handle SERVER_ERROR
            throw new HttpClientErrorException(response.getStatusCode());
        } else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ) {
            // Handle CLIENT_ERROR / 400
            if(response.getStatusCode() == HttpStatus.BAD_REQUEST)
                throw new BadRequestException("RestTemplate Error: Bad Request");
            
            // Handle CLIENT_ERROR / 404
            else if(response.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new NotFoundException("Restempltae Error: URL WebService Not Found.");
            
            
            // Handle CLIENT_ERROR / 401
            else if(response.getStatusCode() == HttpStatus.UNAUTHORIZED)
                throw new InvalidTokenException("Restempltae Error: Invalid Token Request");

            
        }
    }

}
