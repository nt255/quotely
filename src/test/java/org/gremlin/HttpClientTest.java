package org.gremlin;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class HttpClientTest  {

    private HttpClient httpClient;

    @BeforeAll
    void init() {
        this.httpClient = new HttpClient();
    }

    @Test
    void test() {
        HttpClient.Response response = httpClient.get(
                "http://api.forismatic.com/api/1.0/",
                Map.of(
                        "method", "getQuote",
                        "key", "123456",
                        "format", "json",
                        "lang", "en"
                )
        );

        assertEquals(200, response.getCode());
        assertFalse(response.getBody().isEmpty());
    }

}