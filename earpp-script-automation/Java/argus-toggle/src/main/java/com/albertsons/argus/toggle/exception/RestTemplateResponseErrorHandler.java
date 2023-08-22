package com.albertsons.argus.toggle.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

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
                throw new NotFoundException("RestTemplate Error: Not Found URL webservice.");
            
            
            // Handle CLIENT_ERROR / 401
            else if(response.getStatusCode() == HttpStatus.UNAUTHORIZED)
                throw new InvalidTokenException("Restempltae Error: Invalid Token Request");

            
        }
    }

}
