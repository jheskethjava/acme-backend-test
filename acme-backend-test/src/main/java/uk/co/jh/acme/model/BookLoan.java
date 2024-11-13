package uk.co.jh.acme.model;

import java.util.Date;

public record BookLoan(String id, String name, Date begin, Date end) {

}
