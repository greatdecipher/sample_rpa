package com.albertsons.argus.eps.exception;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * 
 * @author damis01
 * @since 9/21/22
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
                throw new BadRequestException("Restempltae Error: bad request");
            
            // Handle CLIENT_ERROR / 404
            else if(response.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new NotFoundException("Restempltae Error: Not Found URL webservice.");
            
            
            // Handle CLIENT_ERROR / 401
            else if(response.getStatusCode() == HttpStatus.UNAUTHORIZED)
                throw new InvalidTokenException("Restempltae Error: Invalid Token Request");

            
        }
    }

}