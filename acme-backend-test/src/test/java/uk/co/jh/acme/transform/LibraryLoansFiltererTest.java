package uk.co.jh.acme.transform;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import uk.co.jh.acme.data.TestData;
import uk.co.jh.acme.model.BookLoan;

class LibraryLoansFiltererTest {
    //@formatter:off
    private static final Date MIN_DATE                   = TestData.stringToDate("2022-01-01T00:00:00.000Z");
    private static final Date EARLIER_THAN_MIN_DATE      = TestData.stringToDate("2021-12-31T23:59:59.999Z");
    private static final Date EVEN_EARLIER_THAN_MIN_DATE = TestData.stringToDate("2020-01-01T00:00:00.000Z");
    private static final Date LATER_THAN_MIN_DATE        = TestData.stringToDate("2022-01-01T00:00:00.001Z");
    private static final Date EVEN_LATER_THAN_MIN_DATE   = TestData.stringToDate("2023-01-01T00:00:00.000Z");
    private static final Date END_DATE                   = TestData.stringToDate("2023-01-01T00:10:00.000Z");
    //@formatter:on

    @Test
    void testFilterOutLoansThatBeganBeforeDate() {
        BookLoan do1 = new BookLoan("1", "1", MIN_DATE, END_DATE);
        BookLoan do2 = new BookLoan("2", "2", EARLIER_THAN_MIN_DATE, END_DATE);
        BookLoan do3 = new BookLoan("3", "3", EVEN_EARLIER_THAN_MIN_DATE, END_DATE);
        BookLoan do4 = new BookLoan("4", "4", LATER_THAN_MIN_DATE, END_DATE);
        BookLoan do5 = new BookLoan("5", "5", EVEN_LATER_THAN_MIN_DATE, END_DATE);

        List<BookLoan> dos = Arrays.asList(do1, do2, do3, do4, do5);

        LibraryLoansFilterer testSubject = new LibraryLoansFilterer();

        List<BookLoan> actual = testSubject.filterOutLoansThatBeganBeforeDate(dos, MIN_DATE);

        assertThat(actual).hasSize(3);
        List<String> actualIds = actual.stream().map(BookLoan::id).toList();
        assertThat(actualIds).contains("1", "4", "5");
    }

}
