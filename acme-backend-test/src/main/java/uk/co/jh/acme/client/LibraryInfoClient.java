package uk.co.jh.acme.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import uk.co.jh.acme.config.AcmeProperties;
import uk.co.jh.acme.model.LibraryInfo;

@Component
public class LibraryInfoClient {
    private static final Logger LOG = LoggerFactory.getLogger(LibraryInfoClient.class);

    private final AcmeProperties acmeProperties;

    private final RestClient restClient;

    public LibraryInfoClient(RestClient.Builder builder, AcmeProperties acmeProperties) {
        this.acmeProperties = acmeProperties;
        this.restClient = builder.baseUrl(acmeProperties.getBaseUrl()).build();
    }

    @Retryable(retryFor = HttpServerErrorException.class)
    public LibraryInfo getLibraryInfo(String libraryId) {
        LibraryInfo libraryInfo = restClient.get()
                .uri("/library-info/" + libraryId)
                .header("x-api-key", acmeProperties.getApiKey())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        LOG.debug("{} library info: {}", libraryId, libraryInfo);
        return libraryInfo;
    }
}
