package uk.co.jh.acme.transform;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import uk.co.jh.acme.data.TestData;
import uk.co.jh.acme.model.BookLoan;
import uk.co.jh.acme.model.LibraryInfo;
import uk.co.jh.acme.model.Loan;

@SpringJUnitConfig(classes = { TestData.class, LibraryLoansTransformer.class })
class LibraryLoansTransformerTest {
    @Autowired
    TestData testData;

    @Autowired
    LibraryLoansTransformer testSubject;

    @Test
    void shouldFilterOutLoansThatAreNotForLibraryAndReturnBookLoansList() {
        // Given
        List<Loan> loans = testData.getLoanList();
        LibraryInfo libraryInfo = testData.getLibraryInfo();

        // When
        List<BookLoan> actual = testSubject.transformLibraryLoans(libraryInfo, loans);

        // Then
        assertThat(actual).hasSize(4);
    }

    @Test
    void shouldHandleEmptyLoanList() {
        // Given
        List<Loan> loans = Collections.emptyList();
        LibraryInfo libraryInfo = testData.getLibraryInfo();

        // When
        List<BookLoan> actual = testSubject.transformLibraryLoans(libraryInfo, loans);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldHandleLibraryWithNoBooks() {
        // Given
        List<Loan> loans = testData.getLoanList();
        LibraryInfo libraryInfo = new LibraryInfo("whatever", "Whatever", null);

        // When
        List<BookLoan> actual = testSubject.transformLibraryLoans(libraryInfo, loans);

        // Then
        assertThat(actual).isEmpty();
    }

}
