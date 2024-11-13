package uk.co.jh.acme.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withTooManyRequests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import uk.co.jh.acme.data.TestData;

@RestClientTest(LibraryLoansClient.class)
@Import(TestData.class)
class LibraryLoansClientTest {
    private static final String LIBRARY_ID = "manchester-central";

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    TestData testData;

    @Autowired
    LibraryLoansClient testSubject;

    @Test
    void shouldPostLibraryLoans() {
        // Given
        givenMockResponse(withSuccess(), 1);

        // When
        String actual = testSubject.postLibraryLoans(LIBRARY_ID, testData.getExpectedOutput());

        // Then
        mockServer.verify();
        assertThat(actual).isNull();

    }

    @Test
    void shouldRetryOnServerError() {
        // Given
        givenMockResponse(withServerError().body("oops"), 3);

        // When
        Throwable throwable = catchThrowable(() -> testSubject.postLibraryLoans(LIBRARY_ID, testData.getExpectedOutput()));

        // Then
        mockServer.verify();
        assertThat(throwable)
                .isInstanceOf(HttpServerErrorException.InternalServerError.class)
                .hasMessage("500 Internal Server Error: \"oops\"");
    }

    @Test
    void shouldNotRetryOnClientError() {
        // Given
        givenMockResponse(withTooManyRequests().body("{ \"message\": \"string\" }"), 1);

        // When
        Throwable throwable = catchThrowable(() -> testSubject.postLibraryLoans(LIBRARY_ID, testData.getExpectedOutput()));

        // Then
        mockServer.verify();
        assertThat(throwable)
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("429 Too Many Requests: \"{ \"message\": \"string\" }\"");
    }

    private void givenMockResponse(DefaultResponseCreator mockResponse, int numberOfTimes) {
        mockServer.expect(ExpectedCount.times(numberOfTimes), requestTo(TestData.BASE_URL + "/library-loans/" + LIBRARY_ID))
                .andExpect(header("x-api-key", TestData.API_KEY))
                .andRespond(mockResponse);
    }
}
