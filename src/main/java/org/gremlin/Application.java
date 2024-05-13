package org.gremlin;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Random;

public class Application {

    private static Logger LOG = LogManager.getLogger(Application.class);

    private static final String DEFAULT_LANGUAGE = "English";
    private static final Map<String, String> LANGUAGE_ARGUMENT_MAP =
            Map.of("English", "en", "Russian", "ru");

    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        Application application = injector.getInstance(Application.class);
        application.start(args);
    }

    private HttpClient httpClient;

    @Inject
    public Application(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @VisibleForTesting
    protected String getRandomNumber() {
        Random rand = new Random();
        int number = rand.nextInt(999999);
        return String.format("%06d", number);
    }

    @VisibleForTesting
    protected QuoteResponse getQuoteResponse(String lang) {
        if (!LANGUAGE_ARGUMENT_MAP.keySet().contains(lang)) {
            throw new RuntimeException("invalid language passed in");
        }

        HttpClient.Response response = httpClient.get(
                "http://api.forismatic.com/api/1.0/",
                Map.of(
                        "format", "json",
                        "method", "getQuote",
                        "key", getRandomNumber(),
                        "lang", LANGUAGE_ARGUMENT_MAP.get(lang)
                )
        );
        QuoteResponse quoteResponse =
                new Gson().fromJson(response.getBody(), QuoteResponse.class);
        return quoteResponse;
    }

    private void start(String[] args) {
        String lang = DEFAULT_LANGUAGE;
        if (args.length > 0) {
            LOG.info("Language argument passed in.");
            lang = args[0];
        }
        LOG.info("Using language {}", lang);

        QuoteResponse quoteResponse = getQuoteResponse(lang);
        System.out.println(quoteResponse.getQuoteText());
        System.out.println(quoteResponse.getQuoteAuthor());
    }

}
