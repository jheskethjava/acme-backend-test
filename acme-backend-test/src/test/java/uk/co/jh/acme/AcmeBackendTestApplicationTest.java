package uk.co.jh.acme;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.co.jh.acme.client.LibraryInfoClient;
import uk.co.jh.acme.client.LibraryLoansClient;
import uk.co.jh.acme.client.LoansClient;
import uk.co.jh.acme.config.AcmeProperties;
import uk.co.jh.acme.data.TestData;
import uk.co.jh.acme.transform.LibraryLoansFilterer;
import uk.co.jh.acme.transform.LibraryLoansTransformer;

@ExtendWith(MockitoExtension.class)
class AcmeBackendTestApplicationTest {
    @Mock
    LoansClient mockLoansClient;

    @Mock
    LibraryInfoClient mockLibraryInfoClient;

    @Mock
    LibraryLoansClient mockLibraryLoansClient;

    AcmeProperties acmeProperties;

    LibraryLoansTransformer transformerTestSubject;

    LibraryLoansFilterer filtererTestSubject;

    AcmeBackendTestApplicationRunner runnerTestSubject;

    TestData testData = new TestData();

    @BeforeEach
    void setUp() {
        acmeProperties = new AcmeProperties();
        acmeProperties.setLoanStartDateMinimum(TestData.stringToDate("2022-01-01T00:00:00.000Z"));

        transformerTestSubject = new LibraryLoansTransformer();
        filtererTestSubject = new LibraryLoansFilterer();
        runnerTestSubject = new AcmeBackendTestApplicationRunner(
                mockLoansClient, mockLibraryInfoClient,
                transformerTestSubject, filtererTestSubject,
                mockLibraryLoansClient, acmeProperties);
    }

    @Test
    void shouldRunEverythingAndPostCorrectBookLoans() {
        // Given
        when(mockLoansClient.getAllLoans()).thenReturn(testData.getLoanList());
        when(mockLibraryInfoClient.getLibraryInfo(any())).thenReturn(testData.getLibraryInfo());
        when(mockLibraryLoansClient.postLibraryLoans(any(), any())).thenReturn("");

        // When
        runnerTestSubject.run("");

        // Then
        verify(mockLoansClient).getAllLoans();
        verify(mockLibraryInfoClient).getLibraryInfo(any());
        verify(mockLibraryLoansClient).postLibraryLoans(any(), eq(testData.getExpectedOutput()));
    }

}
