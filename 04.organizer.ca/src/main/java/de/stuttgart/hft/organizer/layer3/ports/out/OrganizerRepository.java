package de.stuttgart.hft.organizer.layer3.ports.out;

import java.util.List;
import java.util.Optional;

import de.stuttgart.hft.organizer.layer1.domain.model.Task;
import de.stuttgart.hft.organizer.layer2.query.valueobjects.UB_03_Range;
import de.stuttgart.hft.organizer.layer2.query.valueobjects.UB_03_Sort;

public interface OrganizerRepository extends AutoCloseable {

    Optional<Task> readTask(int id);

    List<Task> readTasks();

    List<Task> readTasks(
            Optional<UB_03_Sort> sortSettings,
            Optional<UB_03_Range> rangeSettings);

    Task create(Task task);

    boolean update(Task task);

    boolean delete(int id);

    int countTasks();

    @Override
    void close();
}
