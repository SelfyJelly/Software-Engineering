package UB_03_query.valueobjects;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import domain.model.Task;

public record UB_03_Sort(Column column, Order order) {

    public static final String KEY = "sort";
    public static final String DELIMITER = ".";

    public static enum Column {
        
        ID("Id", "id", t -> t.id()),
        TITLE("Title", "title", t -> t.title()),
        ENTRY_DATE("Entry Date", "entry", t -> t.entry()),
        ESTIMATE("Estimate", "estimate", t -> t.estimate()),
        PRIORITY("Priority", "priority", t -> t.priority()),
        DUE_DATE("Due Date", "due", t -> t.due()),
        DONE("Done", "done", t -> t.done()),
        DESCRIPTION ("Description", "descr", t -> t.description());

        private final String label, value;
        private final Function<Task, Object> getter;

        private Column(String label, String value,
                        Function<Task, Object> getter) {
            this.label = label;
            this.value = value.toLowerCase();
            this.getter = getter;
        }

        private static Map<String, Column> valueMap;
        static {
            valueMap = new HashMap<>();
            for (Column column : values())
                valueMap.put(column.value, column);
        }

        public static Column type(String value) {
            return valueMap.get(value.toLowerCase());
        }

        public String label() {
            return label;
        }

        public Object from(Task task) {
            return getter.apply(task);
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return label;
        }
    }
    public static enum Order {

        UNORDERED("none"), ASCENDING("asc"), DESCENDING("desc");

        private String value;

        private Order(String value) {
            this.value = value.toLowerCase();
        }

        private static Order[] values = values();
        private static Map<String, Order> valueMap;
        static {
            valueMap = new HashMap<>();
            for (Order order : values)
                valueMap.put(order.value, order);
        }

        public String value() {
            return value;
        }

        public Order next() {
            return values[(this.ordinal()+1) % values.length];
        }

        public static Order type(String value) {
            return valueMap.get(value.toLowerCase());
        }
    }

    public static Optional<UB_03_Sort> of(Column column, Order order){
        return Optional.of(new UB_03_Sort(column, order));
    }

    public String key() {
        return KEY;
    }

    public String value() {
        return column.value + DELIMITER + order.value;
    }

    public static UB_03_Sort parseValue(String value) {
        String[] pair = value.split("\\" + DELIMITER);
        if(pair.length != 2)
            return null;
        Column column = Column.type(pair[0].trim());
        Order order = Order.type(pair[1].trim());

        if(column == null || order == null)
            return null;
        return new UB_03_Sort(column, order);
    }
}