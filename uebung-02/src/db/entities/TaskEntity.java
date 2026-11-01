package db.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tasks")
public class TaskEntity {
	
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String ENTRY_DATE = "entry_date";
	public static final String ESTIMATE = "estimate";
	public static final String PRIORITY = "priority";
	public static final String DUE_DATE = "due_date";
	public static final String DONE = "done";
	public static final String DESCRIPTION = "description";

	@DatabaseField(columnName = ID, generatedId = true) private int id;
	@DatabaseField(columnName = TITLE, canBeNull = false) private String title;
	@DatabaseField(columnName = ENTRY_DATE, canBeNull = false) private long entryDate;
	@DatabaseField(columnName = ESTIMATE, canBeNull = false) private int estimate;
	@DatabaseField(columnName = PRIORITY, canBeNull = false) private int priority;
	@DatabaseField(columnName = DUE_DATE, canBeNull = true) private Long dueDate;
	@DatabaseField(columnName = DONE, canBeNull = false) private boolean done;
	@DatabaseField(columnName = DESCRIPTION, dataType = DataType.LONG_STRING, canBeNull = false) private String description;

	public TaskEntity() {

	}

	public TaskEntity(int id, String title, long entryDate, int estimate, int priority, Long dueDate, boolean done,
			String description) {
		super();
		this.id = id;
		this.title = title;
		this.entryDate = entryDate;
		this.estimate = estimate;
		this.priority = priority;
		this.dueDate = dueDate;
		this.done = done;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public long getEntryDate() {
		return entryDate;
	}

	public int getEstimate() {
		return estimate;
	}

	public int getPriority() {
		return priority;
	}

	public Long getDueDate() {
		return dueDate;
	}

	public boolean isDone() {
		return done;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "TaskEntity [id=" + id + ", title=" + title + ", entryDate=" + entryDate + ", estimate=" + estimate
				+ ", priority=" + priority + ", dueDate=" + dueDate + ", done=" + done + ", description=" + description
				+ "]";
	}

}
