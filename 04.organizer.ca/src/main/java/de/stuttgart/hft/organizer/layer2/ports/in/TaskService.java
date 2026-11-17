package de.stuttgart.hft.organizer.layer2.ports.in;

import java.time.LocalDate;
import java.util.List;

import de.stuttgart.hft.organizer.layer1.domain.model.Task;
import de.stuttgart.hft.organizer.layer1.domain.model.Priority;

public interface TaskService {

    int getOpenEstimate(List<Task> tasks);

    List<Task> getPrioritizedTasks(
            List<Task> tasks, Priority priority);

    List<Task> getDueTasks(
            List<Task> tasks, LocalDate date);
}
