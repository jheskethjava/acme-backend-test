package uk.co.jh.acme;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import uk.co.jh.acme.client.LoansClient;
import uk.co.jh.acme.client.LibraryInfoClient;
import uk.co.jh.acme.client.LibraryLoansClient;
import uk.co.jh.acme.config.AcmeProperties;
import uk.co.jh.acme.model.BookLoan;
import uk.co.jh.acme.model.Loan;
import uk.co.jh.acme.model.LibraryInfo;
import uk.co.jh.acme.transform.LibraryLoansFilterer;
import uk.co.jh.acme.transform.LibraryLoansTransformer;

@Component
public class AcmeBackendTestApplicationRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(AcmeBackendTestApplicationRunner.class);

    private final AcmeProperties acmeProperties;

    private final LoansClient loansClient;

    private final LibraryInfoClient libraryInfoClient;

    private final LibraryLoansTransformer libraryLoansTransformer;

    private final LibraryLoansFilterer libraryLoansFilterer;

    private final LibraryLoansClient libraryLoansClient;

    public AcmeBackendTestApplicationRunner(
            LoansClient loansClient,
            LibraryInfoClient libraryInfoClient,
            LibraryLoansTransformer libraryLoansTransformer,
            LibraryLoansFilterer libraryLoansFilterer,
            LibraryLoansClient libraryLoansClient,
            AcmeProperties acmeProperties) {
        super();
        this.loansClient = loansClient;
        this.libraryInfoClient = libraryInfoClient;
        this.libraryLoansTransformer = libraryLoansTransformer;
        this.libraryLoansFilterer = libraryLoansFilterer;
        this.libraryLoansClient = libraryLoansClient;
        this.acmeProperties = acmeProperties;
    }

    @Override
    public void run(String... args) {

        try {
            sendLibraryLoans();
        } catch (RestClientException rce) {
            LOG.error(rce.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        // TODO make this exit gracefully when finished with appropriate exit codes
    }

    private void sendLibraryLoans() {
        List<Loan> loans = loansClient.getAllLoans();

        LibraryInfo libraryInfo = libraryInfoClient.getLibraryInfo(acmeProperties.getLibraryId());

        List<BookLoan> libraryLoans = libraryLoansTransformer.transformLibraryLoans(libraryInfo, loans);

        libraryLoans = libraryLoansFilterer.filterOutLoansThatBeganBeforeDate(libraryLoans, acmeProperties.getLoanStartDateMinimum());

        libraryLoansClient.postLibraryLoans(acmeProperties.getLibraryId(), libraryLoans);
    }
}
