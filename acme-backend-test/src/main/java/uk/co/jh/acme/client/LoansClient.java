package uk.co.jh.acme.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import uk.co.jh.acme.config.AcmeProperties;
import uk.co.jh.acme.model.Loan;

@Component
public class LoansClient {
    private static final Logger LOG = LoggerFactory.getLogger(LoansClient.class);

    private final AcmeProperties acmeProperties;

    private final RestClient restClient;

    public LoansClient(RestClient.Builder builder, AcmeProperties acmeProperties) {
        this.acmeProperties = acmeProperties;
        this.restClient = builder.baseUrl(acmeProperties.getBaseUrl()).build();
    }

    @Retryable(retryFor = HttpServerErrorException.class)
    public List<Loan> getAllLoans() {
        List<Loan> loans = restClient.get()
                .uri("/loans")
                .header("x-api-key", acmeProperties.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        LOG.debug("All loans: {}", loans);
        return loans;
    }
}
