package uk.co.jh.acme.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadGateway;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

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
import uk.co.jh.acme.model.LibraryInfo;

@RestClientTest(LibraryInfoClient.class)
@Import(TestData.class)
class LibraryInfoClientTest {
    private static final String LIBRARY_ID = "manchester-central";

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    TestData testData;

    @Autowired
    LibraryInfoClient testSubject;

    @Test
    void shouldGetLibraryInfo() {
        // Given
        givenMockResponse(withSuccess(TestData.LIBRARY_INFO_RESPONSE, MediaType.APPLICATION_JSON), 1);

        // When
        LibraryInfo actual = testSubject.getLibraryInfo(LIBRARY_ID);

        // Then
        mockServer.verify();
        assertThat(actual).isEqualTo(testData.getLibraryInfo());

    }

    @Test
    void shouldRetryOnServerError() {
        // Given
        givenMockResponse(withBadGateway().body("oops"), 3);

        // When
        Throwable throwable = catchThrowable(() -> testSubject.getLibraryInfo(LIBRARY_ID));

        // Then
        mockServer.verify();
        assertThat(throwable)
                .isInstanceOf(HttpServerErrorException.class)
                .hasMessage("502 Bad Gateway: \"oops\"");
    }

    @Test
    void shouldNotRetryOnClientError() {
        // Given
        givenMockResponse(withResourceNotFound().body("{ \"message\": \"string\" }"), 1);

        // When
        Throwable throwable = catchThrowable(() -> testSubject.getLibraryInfo(LIBRARY_ID));

        // Then
        mockServer.verify();
        assertThat(throwable)
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("404 Not Found: \"{ \"message\": \"string\" }\"");
    }

    private void givenMockResponse(DefaultResponseCreator mockResponse, int numberOfTimes) {
        mockServer.expect(ExpectedCount.times(numberOfTimes), requestTo(TestData.BASE_URL + "/library-info/" + LIBRARY_ID))
                .andExpect(header("x-api-key", TestData.API_KEY))
                .andRespond(mockResponse);
    }
}
