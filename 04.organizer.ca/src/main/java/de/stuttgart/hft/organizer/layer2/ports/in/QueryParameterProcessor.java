package de.stuttgart.hft.organizer.layer2.ports.in;

import java.util.Optional;

import de.stuttgart.hft.organizer.layer2.query.valueobjects.UB_03_Range;
import de.stuttgart.hft.organizer.layer2.query.valueobjects.UB_03_Sort;

public interface QueryParameterProcessor {

    Optional<UB_03_Range> parseRangeValue(Optional<String> value);

    Optional<UB_03_Sort> parseSortValue(Optional<String> value);
}
