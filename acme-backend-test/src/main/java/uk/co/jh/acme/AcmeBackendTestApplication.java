package uk.co.jh.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;

import uk.co.jh.acme.config.AcmeProperties;

@SpringBootApplication
@EnableConfigurationProperties(AcmeProperties.class)
@EnableRetry
public class AcmeBackendTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcmeBackendTestApplication.class, args);
    }

}
