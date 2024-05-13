package org.gremlin;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApplicationTest {

    private Application application;

    @BeforeAll
    void init() {
        HttpClient httpClient = new HttpClient();
        this.application = new Application(httpClient);
    }

    @Test
    void testGetRandomNumber() {
        String randomString = application.getRandomNumber();
        assertTrue(randomString.length() == 6);
    }

    @Test
    void testGetQuoteResponseEnglish() {
        QuoteResponse quoteResponse = application.getQuoteResponse("English");
        assertFalse(quoteResponse.getQuoteText().isEmpty());
    }

    @Test
    void testGetQuoteResponseRussian() {
        QuoteResponse quoteResponse = application.getQuoteResponse("Russian");
        assertFalse(quoteResponse.getQuoteText().isEmpty());
    }

    @Test
    void testGetQuoteResponseInvalid() {
        assertThrows(RuntimeException.class,
                () -> application.getQuoteResponse("German"));
    }
}
