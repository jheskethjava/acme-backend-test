package uk.co.jh.acme.transform;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import uk.co.jh.acme.model.BookLoan;

@Component
public class LibraryLoansFilterer {

    public List<BookLoan> filterOutLoansThatBeganBeforeDate(List<BookLoan> bookLoans, Date minimumDate) {

        return bookLoans.stream()
                .filter(o -> !minimumDate.after(o.begin()))
                .toList();

    }

}
