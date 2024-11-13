package uk.co.jh.mockserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class MockServerController {
    private static final Logger LOG = LoggerFactory.getLogger(MockServerController.class);
    private ObjectMapper mapper;

    public MockServerController(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping(value = "/loans", produces = "application/json")
    public String loans(@RequestHeader("x-api-key") String apiKey) {
        LOG.info("x-api-key: {}", apiKey);

        return """
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
    }

    @GetMapping(value = "/library-info/{library}", produces = "application/json")
    public String libraryInfo(@RequestHeader("x-api-key") String apiKey, @PathVariable String library) {
        LOG.info("x-api-key: {}; library: {}", apiKey, library);

        return """
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
    }

    @PostMapping(value = "/library-loans/{library}", consumes = "application/json")
    public String libraryLoans(@RequestHeader("x-api-key") String apiKey, @PathVariable String library, @RequestBody Object body)
            throws JsonProcessingException {
        String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
        LOG.info("x-api-key: {}; library: {}; \nbody: {}", apiKey, library, pretty);

        return "Thanks!";
    }
}
