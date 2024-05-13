package org.gremlin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuoteResponse {

    private String quoteText;
    private String quoteAuthor;
    private String senderName;
    private String senderLink;
    private String quoteLink;

}
