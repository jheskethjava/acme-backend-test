package uk.co.jh.acme.model;

import java.util.List;

public record LibraryInfo(String id, String name, List<Book> books) {

}
