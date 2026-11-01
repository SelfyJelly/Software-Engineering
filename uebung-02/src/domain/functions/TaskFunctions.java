package domain.functions;

import java.util.List;
import domain.model.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import domain.model.Priority;

public class TaskFunctions {
	public int getOpenEstimate(List<Task> tasks) {
		int sum = 0;
		for (Task task : tasks) {
			if (!task.done()) {
				sum += task.estimate();
			}
		}
		return sum;
	}

	public List<Task> getPrioritizedTasks(List<Task> tasks, Priority priority) {
		List<Task> result = new ArrayList<>();
		for (Task task : tasks) {
			if (task.priority() == priority && !task.done()) {
				result.add(task);
			}
		}
		return result;
	}

	public List<Task> getDueTasks(List<Task> tasks, LocalDate date) {
		List<Task> result = new ArrayList<>();
		for (Task task : tasks) {
			if (!task.done() && task.due() != null && !task.due().isAfter(date)) {
				result.add(task);
			}
		}
		return result;
	}
}