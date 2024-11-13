package uk.co.jh.acme.transform;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import uk.co.jh.acme.model.Book;
import uk.co.jh.acme.model.BookLoan;
import uk.co.jh.acme.model.Loan;
import uk.co.jh.acme.model.LibraryInfo;

@Component
public class LibraryLoansTransformer {

    public List<BookLoan> transformLibraryLoans(LibraryInfo libraryInfo, List<Loan> loans) {
        if (libraryInfo == null || libraryInfo.books() == null || libraryInfo.books().isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Book> libraryBookMap = libraryInfo.books().stream()
                .collect(Collectors.toMap(Book::id, Function.identity()));

        return allBookLoans(loans, libraryBookMap);
    }

    private List<BookLoan> allBookLoans(List<Loan> loans, Map<String, Book> libraryBookMap) {
        return loans.stream()
                .filter(o -> libraryBookMap.containsKey(o.id()))
                .map(o -> mergeModels(o, libraryBookMap.get(o.id())))
                .toList();
    }

    private BookLoan mergeModels(Loan loan, Book book) {
        return new BookLoan(loan.id(), book.name(), loan.begin(), loan.end());
    }

}
