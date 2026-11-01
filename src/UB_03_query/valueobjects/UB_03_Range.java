package UB_03_query.valueobjects;

import java.util.Optional;

public record UB_03_Range(long offset, long limit) {

public static final String KEY = "items";
public static final String DELIMITER = ":";

public static Optional<UB_03_Range> of(long offset, long limit) {
    return Optional.of(new UB_03_Range(offset, limit));
}

public String key() {
    return KEY;
}

public String value() {
    return offset + DELIMITER + limit;
}

public static UB_03_Range parseValue(String value) {
    String[] pair = value.split("\\" + DELIMITER);
    if(pair.length != 2)
        return null;
    try {
        long offset = Long.parseLong(pair[0].trim());
        long limit = Long.parseLong(pair[1].trim());

        if(offset < 0 || limit < 0) return null;
            return new UB_03_Range(offset, limit);
    } catch (NumberFormatException e) {
        return null;
        }
    }
}
