# Acme Back End Test
Anonymised interview exercise - same code, different domain and company name.

## Introduction

This test is designed to help you showcase your back end engineering skills. We are interested to see how you work and
what your thought process is, there is no single correct way to complete the task.

## The Task

There are three endpoints:

1. `GET /loans` which returns all loans in our system
2. `GET /library-info/{libraryId}` which returns specific information about a library
3. `POST /library-loans/{libraryId}` which expects loans for a specific library to be posted to it

Your task is to write a small program that:

1. Retrieves all loans from the `GET /loans` endpoint
2. Retrieves information from the `GET /library-info/{libraryId}` endpoint for the library with the ID `bodlean`
3. Filters out any loans that began before `2022-01-01T00:00:00.000Z` or don't have an ID that is in the list of
   books in the library information
4. For the remaining loans, it should attach the display name of the book in the library information to each appropriate loan
5. Sends this list of loans to `POST /library-loans/{libraryId}` for the library with the ID `bodlean`


### Example

Let's assume we want to do this for the library `manchester-central`.

Given `GET /loans` returns:

```json
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
```

Given `GET /library-info/manchester-central` returns:

```json
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
```

We should send the following to `POST /library-loans/manchester-central`:

```json
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
```

### Requirements

We would like you to produce:

* A small program that solves the problem mentioned above
* A suite of appropriate unit tests
* A `README.md` file that documents what you have produced and explains how to run the program and tests (including installing any dependencies)

* Once you have a complete solution, please commit into a git repo that can be accessed by Acme. Email your recruitment contact with a link to this repository so that your submission can be reviewed. 

Thank you. 

### Bonus Requirements

* The API will occasionally return 500 status codes. Can you implement a solution that is resilient to this scenario?

### Tips and Things to Note

* Make sure to include the provided API key in each request that you send. For example, run the following to test
  this out:
  ```bash
  curl https://api.acme.systems/interview-tests-mock-api/v1/loans -H "x-api-key: <API KEY>"
  ```
* Each endpoint has the base path `https://api.acme.systems/interview-tests-mock-api/v1/`.
* If you are unsure of the schemas required, take a look at the `api.yaml` provided.
* You will only get a 200 success response from `POST /library-loans/{libraryId}` if the payload is correct.

**Good luck!**
