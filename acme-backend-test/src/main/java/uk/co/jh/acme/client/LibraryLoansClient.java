package uk.co.jh.acme.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.jh.acme.config.AcmeProperties;
import uk.co.jh.acme.model.BookLoan;

@Component
public class LibraryLoansClient {
    private static final Logger LOG = LoggerFactory.getLogger(LibraryLoansClient.class);

    private final AcmeProperties acmeProperties;

    private final RestClient restClient;

    private final ObjectMapper mapper;

    public LibraryLoansClient(RestClient.Builder builder, AcmeProperties acmeProperties, ObjectMapper mapper) {
        this.acmeProperties = acmeProperties;
        this.restClient = builder.baseUrl(acmeProperties.getBaseUrl()).build();
        this.mapper = mapper;
    }

    @Retryable(retryFor = HttpServerErrorException.class)
    public String postLibraryLoans(String libraryId, @RequestBody List<BookLoan> bookLoans) {
        logRequestBody(bookLoans);

        String response = restClient.post()
                .uri("/library-loans/" + libraryId)
                .header("x-api-key", acmeProperties.getApiKey())
                .body(bookLoans)
                .retrieve()
                .body(String.class);

        LOG.info("Happy dance! Library loans payload accepted! {}", response);
        return response;
    }

    private void logRequestBody(List<BookLoan> bookLoans) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sending library loans: {}", mapper.writeValueAsString(bookLoans));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
