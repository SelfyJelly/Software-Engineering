package file.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import domain.model.Priority;

import domain.model.Task;

public class TaskReader {

	public static List<Task> fromCsv(String filepath) {
		List<Task> tasks = new ArrayList<>();
		InputStream is = TaskReader.class.getResourceAsStream(filepath);

		if (is == null) {
			System.out.println("Datei nicht gefunden: " + filepath);
			return tasks;
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

			reader.readLine();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(";");
				String title = values[0];
				LocalDate entry = LocalDate.parse(values[1]);
				int estimate = Integer.parseInt(values[2]);
				Priority priority = Priority.valueOf(values[3].toUpperCase());
				String dueText = values[4];
				LocalDate due = null;
				if (!dueText.equalsIgnoreCase("null") && !dueText.equalsIgnoreCase("ongoing")) {
					due = LocalDate.parse(dueText);
				}
				boolean done = values[5].equalsIgnoreCase("done");
				String description = values[6];
				Task task = Task.of(title, entry, estimate, priority, due, done, description);
				tasks.add(task);
			}

		} catch (IOException e) {
			System.out.println("Fehler beim Lesen: " + e.getMessage());
		}
		return tasks;
	}
}
