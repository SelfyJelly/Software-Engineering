package db.access;

import java.sql.SQLException;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ORMLiteUtilities {

	private static boolean tableExists(ConnectionSource connectionSource, Class<?> entityClass) {
		try {// if the table exists, this query succeeds.
			DaoManager.createDao(connectionSource, entityClass).queryForFirst();
			return true;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
	}

	public static boolean createTableIfNotExists(String dbUrl, Class<?> entityClass) {
		try (ConnectionSource connectionSource = new JdbcConnectionSource(dbUrl)) {
			// This is a workaround, as createTableIfNotExists gives an error with Postgres
			// https://github.com/j256/ormlite-core/issues/20
			if (!tableExists(connectionSource, entityClass))
				TableUtils.createTable(connectionSource, entityClass);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean dropTableIfExists(String dbUrl, Class<?> entityClass) {
		try (ConnectionSource connectionSource = new JdbcConnectionSource(dbUrl)) {
			if (tableExists(connectionSource, entityClass))
				TableUtils.dropTable(connectionSource, entityClass, true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean resetTable(String dbUrl, Class<?> entityClass) {
		try (ConnectionSource connectionSource = new JdbcConnectionSource(dbUrl)) {
			if (tableExists(connectionSource, entityClass))
				TableUtils.dropTable(connectionSource, entityClass, true);
			TableUtils.createTable(connectionSource, entityClass);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
