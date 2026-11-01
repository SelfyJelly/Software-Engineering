package usecases;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import UB_03_query.valueobjects.UB_03_Range;
import UB_03_query.valueobjects.UB_03_Sort;
import db.access.ORMLiteOrganizerRepository;
import domain.functions.TaskFunctions;
import domain.model.Priority;
import domain.model.Task;

public class OrganizerUseCases {

	private final ORMLiteOrganizerRepository repository;
	private final TaskFunctions functions;

	public OrganizerUseCases(ORMLiteOrganizerRepository repository, TaskFunctions functions) {
		this.repository = repository;
		this.functions = functions;
	}

	public Task getTask(int id) {
		return repository.readTask(id).orElse(null);
	}

	public List<Task> getTasks() {
		return repository.readTasks();
	}

	public int getNumberOfTasks() {
		return repository.countTasks();
	}

	public Task enter(Task element) {
		return repository.create(element);
	}

	public boolean update(Task element) {
		return repository.update(element);
	}

	public boolean delete(int id) {
		return repository.delete(id);
	}

	public List<Task> getDueTasks(LocalDate date) {
		List<Task> tasks = repository.readTasks();
		return functions.getDueTasks(tasks, date);
	}

	public List<Task> getPrioritizedTasks(Priority priority) {
		List<Task> tasks = repository.readTasks();
		return functions.getPrioritizedTasks(tasks, priority);
	}

	public int getOpenEstimate() {
		List<Task> tasks = repository.readTasks();
		return functions.getOpenEstimate(tasks);
	}

	// Übung 03 eingefügt 
	public List<Task> getTasks(Optional<UB_03_Sort> sortSettings, Optional<UB_03_Range> rangeSettings) {
		return repository.readTasks(sortSettings, rangeSettings);
	}

}
