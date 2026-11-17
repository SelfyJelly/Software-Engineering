package de.stuttgart.hft.organizer.layer4.db.mapper;

import static de.stuttgart.hft.organizer.layer1.domain.model.Priority.*;

import java.time.LocalDate;

import de.stuttgart.hft.organizer.layer4.db.entities.TaskEntity;
import de.stuttgart.hft.organizer.layer1.domain.model.Priority;
import de.stuttgart.hft.organizer.layer1.domain.model.Task;
import de.stuttgart.hft.organizer.layer4.mapping.framework.EnumMapper;
import de.stuttgart.hft.organizer.layer4.mapping.framework.Mapper;

public class TaskMapper implements Mapper<Task, TaskEntity> {

	private EnumMapper<Priority> priorityMapper = EnumMapper.of(NONE, LOW, MEDIUM, HIGH);

	@Override
	public Task toDomain(TaskEntity entity) {
		return new Task(entity.getId(), entity.getTitle(), LocalDate.ofEpochDay(entity.getEntryDate()),
				entity.getEstimate(), priorityMapper.toEnum(entity.getPriority()),
				(entity.getDueDate() == null ? null : LocalDate.ofEpochDay(entity.getDueDate())), entity.isDone(),
				entity.getDescription());
	}

	@Override
	public TaskEntity toEntity(Task element) {
		return new TaskEntity(element.id(), element.title(), element.entry().toEpochDay(), element.estimate(),
				priorityMapper.toInt(element.priority()), (element.due() == null ? null : element.due().toEpochDay()),
				element.done(), element.description());
	}
}