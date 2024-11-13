package uk.co.jh.acme.data;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.jh.acme.model.BookLoan;
import uk.co.jh.acme.model.LibraryInfo;
import uk.co.jh.acme.model.Loan;

@Component
public class TestData {

    private ObjectMapper mapper = new ObjectMapper();

    public static final String API_KEY = "testApiKey";

    public static final String BASE_URL = "http://localhost:8070";

    public static final String LOAN_RESPONSE = """
            [
              {
                "id": "002b28fc-283c-47ec-9af2-ea287336dc1b",
                "begin": "2021-07-26T17:09:31.036Z",
                "end": "2021-08-29T00:37:42.253Z"
              },
              {
                "id": "002b28fc-283c-47ec-9af2-ea287336dc1b",
                "begin": "2022-05-23T12:21:27.377Z",
                "end": "2022-11-13T02:16:38.905Z"
              },
              {
                "id": "002b28fc-283c-47ec-9af2-ea287336dc1b",
                "begin": "2022-12-04T09:59:33.628Z",
                "end": "2022-12-12T22:35:13.815Z"
              },
              {
                "id": "04ccad00-eb8d-4045-8994-b569cb4b64c1",
                "begin": "2022-07-12T16:31:47.254Z",
                "end": "2022-10-13T04:05:10.044Z"
              },
              {
                "id": "086b0d53-b311-4441-aaf3-935646f03d4d",
                "begin": "2022-07-12T16:31:47.254Z",
                "end": "2022-10-13T04:05:10.044Z"
              },
              {
                "id": "27820d4a-1bc4-4fc1-a5f0-bcb3627e94a1",
                "begin": "2021-07-12T16:31:47.254Z",
                "end": "2022-10-13T04:05:10.044Z"
              }
            ]
            """;

    public static final String LIBRARY_INFO_RESPONSE = """
            {
              "id": "manchester-central",
              "name": "Manchester Central",
              "books": [
                {
                  "id": "002b28fc-283c-47ec-9af2-ea287336dc1b",
                  "name": "Book 1"
                },
                {
                  "id": "086b0d53-b311-4441-aaf3-935646f03d4d",
                  "name": "Book 2"
                }
              ]
            }
            """;

    public static final String EXPECTED_OUTPUT = """
            [
              {
                "id": "002b28fc-283c-47ec-9af2-ea287336dc1b",
                "name": "Book 1",
                "begin": "2022-05-23T12:21:27.377Z",
                "end": "2022-11-13T02:16:38.905Z"
              },
              {
                "id": "002b28fc-283c-47ec-9af2-ea287336dc1b",
                "name": "Book 1",
                "begin": "2022-12-04T09:59:33.628Z",
                "end": "2022-12-12T22:35:13.815Z"
              },
              {
                "id": "086b0d53-b311-4441-aaf3-935646f03d4d",
                "name": "Book 2",
                "begin": "2022-07-12T16:31:47.254Z",
                "end": "2022-10-13T04:05:10.044Z"
              }
            ]
            """;

    public List<Loan> getLoanList() {
        try {
            return mapper.readValue(LOAN_RESPONSE, new TypeReference<List<Loan>>() {
            });
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public LibraryInfo getLibraryInfo() {
        try {
            return mapper.readValue(LIBRARY_INFO_RESPONSE, LibraryInfo.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public List<BookLoan> getExpectedOutput() {
        try {
            return mapper.readValue(EXPECTED_OUTPUT, new TypeReference<List<BookLoan>>() {
            });
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static Date stringToDate(String dateString) {
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
}
