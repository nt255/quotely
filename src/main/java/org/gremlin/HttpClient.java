package org.gremlin;

import static java.net.HttpURLConnection.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Builder;
import lombok.Getter;

public class HttpClient {

    private static final Logger LOG =
            LogManager.getLogger(HttpClient.class);

    private static final String GET = "GET";

    @Builder
    @Getter
    public static class Response {
        private final int code;
        private final String body;
    }

    public Response get(String url, Map<String, String> queryParams) {
        try {
            if (!queryParams.isEmpty()) {
                String paramsString = queryParams.entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .collect(Collectors.joining("&"));
                url += "?" + paramsString;
            }

            LOG.info("Calling {}", url);

            URL obj = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod(GET);

            // ------ response
            InputStream inputStream;
            int code = conn.getResponseCode();
            if (code == HTTP_BAD_REQUEST || code == HTTP_NOT_FOUND)
                inputStream = conn.getErrorStream();
            else
                inputStream = conn.getInputStream();

            if (inputStream == null)
                return getResponse(code, "");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(inputStream));

            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();

            return getResponse(code, sb.toString());

        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Response getResponse(int code, String body) {
        Response response = Response.builder()
                .code(code)
                .body(body)
                .build();

        LOG.info("Received code: {}", response.getCode());
        if (body.isEmpty())
            LOG.info("empty body");
        else
            LOG.info("Received body: {}", body);

        return response;
    }
}

