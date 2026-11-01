package UB_03_query.functions;
import static UB_03_query.valueobjects.UB_03_Sort.Column.*;

import java.util.List;
import java.util.Optional;

import UB_03_query.valueobjects.UB_03_Range;
import UB_03_query.valueobjects.UB_03_Sort;
import UB_03_query.valueobjects.UB_03_Sort.Column;

public class UB_03_QueryParameterFunctions {

    public List<Column> headers(){
        return List.of(ID, TITLE, ENTRY_DATE, ESTIMATE, PRIORITY, DUE_DATE,
DONE);
    }

    public List<Column> attributes() {
        return List.of(ID, TITLE, ENTRY_DATE, ESTIMATE, PRIORITY, DUE_DATE,
DONE, DESCRIPTION);
    }

    public Optional<UB_03_Range> parseRangeValue(Optional<String> value) {
        if(value.isEmpty())
            return Optional.empty();

        UB_03_Range range = UB_03_Range.parseValue(value.get());
        if(range == null)
            return Optional.empty();
        else
            return Optional.of(range);
    }

public Optional<UB_03_Sort> parseSortValue(Optional<String> value) {
    if(value.isEmpty())
        return Optional.empty();

    UB_03_Sort sort = UB_03_Sort.parseValue(value.get());
        if(sort == null)
            return Optional.empty();
        else
            return Optional.of(sort);
    }
}
