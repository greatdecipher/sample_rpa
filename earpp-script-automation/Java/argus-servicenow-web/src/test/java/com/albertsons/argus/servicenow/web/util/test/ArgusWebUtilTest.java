package com.albertsons.argus.servicenow.web.util.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.albertsons.argus.ArgusServiceNowTestApplication;
import com.albertsons.argus.servicenow.web.util.ArgusWebUtil;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ArgusServiceNowTestApplication.class)
public class ArgusWebUtilTest {
    @Test
    public void testDecode() {
        ArgusWebUtil automationUtil = new ArgusWebUtil();
        String testUrl = "http://www.safeway.com?test=test1%3D2%5Etest3";

        URI uri;
        try {
            uri = new URI(testUrl);

            String scheme = uri.getScheme();
            String host = uri.getHost();
            String query = uri.getRawQuery();

            String decodedQuery = Arrays.stream(query.split("&"))
                    .map(param -> param.split("=")[0] + "=" + automationUtil.decodeUri(param.split("=")[1]))
                    .collect(Collectors.joining("&"));

            assertEquals("http://www.safeway.com?test=test1=2^test3", scheme + "://" + host + "?" + decodedQuery);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testEncode() {
        ArgusWebUtil automationUtil = new ArgusWebUtil();
        String testUrl = "http://www.safeway.com?test2=value%253&test=test1%3D2%5Etest3";

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("test2", "value%3");
        requestParams.put("test", "test1=2^test3");

        String encodedURL = requestParams.keySet().stream()
                .map(key -> key + "=" + automationUtil.encodeUri(requestParams.get(key)))
                .collect(Collectors.joining("&", "http://www.safeway.com?", ""));

        assertEquals(testUrl, encodedURL);
    }

    @Test
    public void testPathEncode() {
        ArgusWebUtil automationUtil = new ArgusWebUtil();
        String pathSegment = "/Path 1/Path+2";
        String encodedPathSegment = automationUtil.encodePath(pathSegment);
        String decodedPathSegment = automationUtil.decodeUri(encodedPathSegment);

        assertEquals("/Path%201/Path+2", encodedPathSegment);
        assertEquals("/Path 1/Path+2", decodedPathSegment);
    }

}
