package de.stuttgart.hft.organizer.layer2.ports.in;

import java.util.List;

import de.stuttgart.hft.organizer.layer2.query.valueobjects.UB_03_Sort.Column;

public interface QueryParameterBuilder {

    public static String COLUMN_KEY = "column";
    public static String PAGE_KEY = "page";

    List<Column> headers();

    List<Column> attributes();

    default String columnKey() {
        return COLUMN_KEY;
    }

    default String pageKey() {
        return PAGE_KEY;
    }
}
