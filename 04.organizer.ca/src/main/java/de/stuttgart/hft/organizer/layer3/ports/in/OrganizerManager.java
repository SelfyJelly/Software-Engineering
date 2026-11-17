package de.stuttgart.hft.organizer.layer3.ports.in;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import de.stuttgart.hft.organizer.layer1.domain.model.Task;
import de.stuttgart.hft.organizer.layer1.domain.model.Priority;
import de.stuttgart.hft.organizer.layer2.query.valueobjects.UB_03_Sort;
import de.stuttgart.hft.organizer.layer2.query.valueobjects.UB_03_Range;

public interface OrganizerManager {

    Task getTask(int id);

    List<Task> getTasks();

    int getNumberOfTasks();

    Task enter(Task element);

    boolean update(Task element);

    boolean delete(int id);

    List<Task> getDueTasks(LocalDate date);

    List<Task> getPrioritizedTasks(Priority priority);

    int getOpenEstimate();

    List<Task> getTasks(
            Optional<UB_03_Sort> sortSettings,
            Optional<UB_03_Range> rangeSettings);
}
