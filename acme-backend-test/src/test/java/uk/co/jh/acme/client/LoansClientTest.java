package uk.co.jh.acme.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withForbiddenRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withGatewayTimeout;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import uk.co.jh.acme.data.TestData;
import uk.co.jh.acme.model.Loan;

@RestClientTest(LoansClient.class)
@Import(TestData.class)
class LoansClientTest {

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    TestData testData;

    @Autowired
    LoansClient testSubject;

    @Test
    void shouldGetAllLoans() {
        // Given
        givenMockResponse(withSuccess(TestData.LOAN_RESPONSE, MediaType.APPLICATION_JSON), 1);

        // When
        List<Loan> actual = testSubject.getAllLoans();

        // Then
        mockServer.verify();
        assertThat(actual)
                .hasSize(6)
                .containsExactlyElementsOf(testData.getLoanList());

    }

    @Test
    void shouldRetryOnServerError() {
        // Given
        givenMockResponse(withGatewayTimeout().body("oops"), 3);

        // When
        Throwable throwable = catchThrowable(() -> testSubject.getAllLoans());

        // Then
        mockServer.verify();
        assertThat(throwable)
                .isInstanceOf(HttpServerErrorException.class)
                .hasMessage("504 Gateway Timeout: \"oops\"");
    }

    @Test
    void shouldNotRetryOnClientError() {
        // Given
        givenMockResponse(withForbiddenRequest().body("{ \"message\": \"string\" }"), 1);

        // When
        Throwable throwable = catchThrowable(() -> testSubject.getAllLoans());

        // Then
        mockServer.verify();
        assertThat(throwable)
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("403 Forbidden: \"{ \"message\": \"string\" }\"");
    }

    private void givenMockResponse(DefaultResponseCreator mockResponse, int numberOfTimes) {
        mockServer.expect(ExpectedCount.times(numberOfTimes), requestTo(TestData.BASE_URL + "/loans"))
                .andExpect(header("x-api-key", TestData.API_KEY))
                .andRespond(mockResponse);
    }
}
