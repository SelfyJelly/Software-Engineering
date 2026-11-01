package db.access;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;

import UB_03_query.valueobjects.UB_03_Range;
import UB_03_query.valueobjects.UB_03_Sort;
import UB_03_query.valueobjects.UB_03_Sort.Column;
import UB_03_query.valueobjects.UB_03_Sort.Order;
import db.entities.TaskEntity;
import db.mapper.TaskMapper;
import db.mapper.UB_03_ColumnMapper;
import domain.model.Task;

public class ORMLiteOrganizerRepository implements AutoCloseable {

	private final JdbcConnectionSource connection;
	private final Dao<TaskEntity, Integer> taskDao;
	private final TaskMapper taskMapper;
	private final UB_03_ColumnMapper columnMapper;

	public ORMLiteOrganizerRepository(String dbUrl) {
		boolean created = ORMLiteUtilities.createTableIfNotExists(dbUrl, TaskEntity.class);
		if (!created) {
			throw new IllegalStateException("Could not initialize task table");
		}
		JdbcConnectionSource newConnection = null;
		try {
			newConnection = new JdbcConnectionSource(dbUrl);
			this.connection = newConnection;
			this.taskDao = DaoManager.createDao(connection, TaskEntity.class);
			this.taskMapper = new TaskMapper();
			this.columnMapper = new UB_03_ColumnMapper();
		} catch (SQLException initializationException) {
			if (newConnection != null) {
				try {
					newConnection.close();
				} catch (Exception closingException) {
					initializationException.addSuppressed(closingException);
				}
			}
			throw new IllegalStateException("Could not initialize organizer repository", initializationException);
		}
	}

	public Optional<Task> readTask(int id) {
		try {
			TaskEntity entity = taskDao.queryForId(id);
			if(entity == null) return Optional.empty();			
			Task element = taskMapper.toDomain(entity);
			return Optional.of(element);
		} catch (SQLException e) {
			throw new IllegalStateException("Could not read task " + id, e);
		}
	}

	public List<Task> readTasks() {
		try {
			List<TaskEntity> entities = taskDao.queryBuilder().orderBy(TaskEntity.ID, true).query();
			List<Task> elements = taskMapper.toDomain(entities);
			return elements;
		} catch (SQLException e) {
			throw new IllegalStateException("Could not read tasks", e);
		}
	}

	public Task create(Task task) {
		try {
			TaskEntity entity = taskMapper.toEntity(task);
			int count = taskDao.create(entity);
			// create() updates the initially empty generated ID

			if (count != 1)
				throw new IllegalStateException("Expected one created task, but got " + count);

			return taskMapper.toDomain(entity);
		} catch (SQLException e) {
			throw new IllegalStateException("Could not create task", e);
		}
	}

	public boolean update(Task task) {
		try {

			TaskEntity entity = taskMapper.toEntity(task);
			int count = taskDao.update(entity);
			if (count == 1) {
				return true;
			}
			if (count == 0) {
				return false;
			}
			throw new IllegalStateException("Unexpected update count: " + count);

		} catch (SQLException e) {
			throw new IllegalStateException("Could not update task " + task.id(), e);
		}
	}

	public boolean delete(int id) {
		try {
			
			int count = taskDao.deleteById(id);
			if (count == 1) {
				return true;
			}
			if (count == 0) {
				return false;
			}
			throw new IllegalStateException("Unexpected delete count: " + count);

		} catch (SQLException e) {
			throw new IllegalStateException("Could not delete task " + id, e);
		}
	}

	public int countTasks() {
		try {

			long count = taskDao.countOf();
			return Math.toIntExact(count);

		} catch (SQLException e) {
			throw new IllegalStateException("Could not count tasks", e);
		}
	}

	@Override
	public void close() {
		try {
			connection.close();
		} catch (Exception e) {
			throw new IllegalStateException("Could not close task repository", e);
		}
	}

	// Übung 03 eingefügt 
	public List<Task> readTasks(Optional<UB_03_Sort> sortSettings, Optional<UB_03_Range> rangeSettings) {
try {
QueryBuilder<TaskEntity, Integer> qb = taskDao.queryBuilder();
if(sortSettings.isEmpty() || sortSettings.get().order() == Order.UNORDERED)
qb.orderBy(TaskEntity.ID, true);
else {
String column = columnMapper.toEntity(sortSettings.get().column());
boolean ascending = sortSettings.get().order() == Order.ASCENDING;
qb.orderBy(column, ascending);
if(sortSettings.get().column() != Column.ID)
qb.orderBy(TaskEntity.ID, true);
}
if(rangeSettings.isPresent()) {
long offset = rangeSettings.get().offset();
long limit = rangeSettings.get().limit();
qb.offset(offset).limit(limit);
}
List<TaskEntity> entities = qb.query();
List<Task> elements = taskMapper.toDomain(entities);
return elements;
} catch (SQLException e) {
throw new IllegalStateException("Could not read tasks", e);
}
}
}
