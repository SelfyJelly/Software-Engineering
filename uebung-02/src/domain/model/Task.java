package domain.model;

import java.time.LocalDate;

public record Task(int id, String title, LocalDate entry, int estimate, Priority priority, LocalDate due, boolean done,
		String description) {

	public static Task of(String title, LocalDate entry, int estimate, Priority priority, LocalDate due, boolean done,
			String description) {
		return new Task(0, title, entry, estimate, priority, due, done, description);
	}
}
