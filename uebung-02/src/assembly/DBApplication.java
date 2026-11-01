package assembly;

import java.time.LocalDate;
import java.util.List;

import db.access.ORMLiteOrganizerRepository;
import domain.functions.TaskFunctions;
import domain.model.Priority;
import domain.model.Task;
import file.access.TaskReader;
import usecases.OrganizerUseCases;

public class DBApplication {

	public static void main(String[] args) {
		try (ORMLiteOrganizerRepository repository = new ORMLiteOrganizerRepository("jdbc:sqlite:organizer.db")) {

			if (repository.countTasks() == 0) {
				System.out.println("Creating Tasks ...");
				List<Task> tasks = TaskReader.fromCsv("/samples/SampleTasks.csv");
				for (Task task : tasks) {
					repository.create(task);
				}
			}

			TaskFunctions functions = new TaskFunctions();
			OrganizerUseCases manager = new OrganizerUseCases(repository, functions);

			List<Task> tasks = manager.getTasks();

			System.out.println("All Tasks:");
			for (Task task : tasks) {
				System.out.println(task);
			}

			System.out.println();
			System.out.println("Open estimate: " + manager.getOpenEstimate() + " hours");

			System.out.println();
			System.out.println("Medium Priority Tasks:");
			for (Task task : manager.getPrioritizedTasks(Priority.MEDIUM)) {
				System.out.println(task);
			}

			System.out.println();
			System.out.println("Due Tasks by 2026-12-31:");
			for (Task task : manager.getDueTasks(LocalDate.of(2026, 12, 31))) {
				System.out.println(task);
			}

			System.out.println();
			System.out.println("===== ZUSAMMENFASSUNG =====");
			System.out.println("Anzahl aller Aufgaben: " + manager.getNumberOfTasks());
			System.out.println("Offener Gesamtaufwand: " + manager.getOpenEstimate() + " Stunden");

		}
	}
}
