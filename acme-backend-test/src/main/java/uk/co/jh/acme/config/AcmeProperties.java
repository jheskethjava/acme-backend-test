package uk.co.jh.acme.config;

import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;

@ConfigurationProperties("acme")
public class AcmeProperties {

    /**
     * The base path for the Acme Back End Test
     */
    private String baseUrl;

    /**
     * The API key supplied for the Acme Back End Test
     */
    private String apiKey;

    /**
     * The library ID to use for the Acme Back End Test
     */
    private String libraryId;

    /**
     * Earliest loan start date in the format yyyy-MM-dd'T'HH:mm:ss.SSSX
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date loanStartDateMinimum;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public Date getLoanStartDateMinimum() {
        return loanStartDateMinimum;
    }

    public void setLoanStartDateMinimum(Date loanStartDateMinimum) {
        this.loanStartDateMinimum = loanStartDateMinimum;
    }

}
