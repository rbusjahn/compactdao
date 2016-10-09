package org.rbusjahn.compactdao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import javafx.util.Pair;

public class GenericDao<T> implements IGenericDao<T> {

	protected static Logger LOG = Logger.getLogger(GenericDao.class);
	protected static String driver = "org.apache.derby.jdbc.EmbeddedDriver";

	private Dao<T, Long> dao;

	protected Class<?> clazz;
	private String tableName;
	private String idColumn;
	private JdbcConnectionSource connectionSource;
	private static JdbcConnectionSource defaultJdbcConnectionSource;

	public GenericDao(Class<T> t, ConnectionSettings settings) {
		try {
			this.clazz = t;

			init(settings);

			this.dao = DaoManager.createDao(this.connectionSource, t);
		} catch (final SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public GenericDao(Class<T> t, JdbcConnectionSource connectionSource) {
		try {
			this.clazz = t;

			this.connectionSource = connectionSource;

			this.dao = DaoManager.createDao(connectionSource, t);

		} catch (final SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public GenericDao(Class<T> t) {
		try {
			this.clazz = t;

			this.connectionSource = getDefaultJdbcConnectionSource();

			this.dao = DaoManager.createDao(connectionSource, t);

		} catch (final SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void init(ConnectionSettings settings) {
		try {
			Class.forName(driver).newInstance();

			final String databaseUrl = MessageFormat.format("jdbc:derby:{0};{1}", settings.getDatabaseFolderName(),
					settings.getDerbyConnectionParameter());

			LOG.debug("using database url: " + databaseUrl);

			this.connectionSource = new JdbcConnectionSource(databaseUrl);
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static JdbcConnectionSource getDefaultJdbcConnectionSource() {
		if (defaultJdbcConnectionSource == null) {
			if (DaoEnvironment.useInMemoryDatabase()) {
				defaultJdbcConnectionSource = getJdbcConnectionSource(new InMemoryConnectionSettings());
			} else {
				defaultJdbcConnectionSource = getJdbcConnectionSource(new DefaultConnectionSettings());
			}
		}
		return defaultJdbcConnectionSource;
	}

	public static JdbcConnectionSource getJdbcConnectionSource(ConnectionSettings settings) {
		try {
			Class.forName(driver).newInstance();

			final String databaseUrl = MessageFormat.format("jdbc:derby:{0};{1}", settings.getDatabaseFolderName(),
					settings.getDerbyConnectionParameter());

			LOG.debug("using database url: " + databaseUrl);

			return new JdbcConnectionSource(databaseUrl);
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void save(T t) {
		try {
			dao.createOrUpdate(t);
		} catch (final SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void delete(T t) {
		try {
			dao.delete(t);
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public T getById(Long id) {
		T value = null;
		try {
			value = dao.queryForId(id);
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public List<T> getByIdList(List<Long> ids) {
		List<T> list = new ArrayList<>();
		QueryBuilder<T, Long> builder = dao.queryBuilder();
		final Where<T, Long> where = builder.where();
		try {
			where.in(getIdColumn(), ids);
			builder = builder.orderBy(getIdColumn(), true);
			final PreparedQuery<T> query = builder.prepare();
			list = dao.query(query);
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void createTable() {
		createTable(false);
	}

	public void createTable(boolean throwError) {
		try {
			TableUtils.createTableIfNotExists(dao.getConnectionSource(), this.clazz);
		} catch (final SQLException e) {
			LOG.warn(e.getMessage());
			if (throwError) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	public void runUpdateSql(String statement) {
		try {

			dao.executeRawNoArgs(statement);

		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void save(List<T> list) {
		try {
			dao.callBatchTasks(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for (final T t : list) {
						dao.create(t);
					}
					return null;
				}
			});
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	@Override
	public List<T> findAll() {
		List<T> result = Collections.emptyList();
		try {
			QueryBuilder<T, Long> builder = dao.queryBuilder();
			builder = builder.orderBy(getIdColumn(), true);
			final PreparedQuery<T> query = builder.prepare();
			result = dao.query(query);
		} catch (final SQLException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}

	public void iterate() {
		getTableName();
	}

	protected String getTableName() {
		if (tableName == null) {
			final DatabaseTable databaseTableAnnotation = clazz.getAnnotation(DatabaseTable.class);
			if (databaseTableAnnotation != null) {
				tableName = databaseTableAnnotation.tableName();
			}
		}
		return tableName;
	}

	protected String getIdColumn() {
		if (idColumn != null) {
			return idColumn;
		}
		for (final Field field : clazz.getDeclaredFields()) {
			final DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
			if ((databaseField != null) && databaseField.generatedId()) {
				idColumn = field.getName();
				if ((databaseField.columnName() != null) && (databaseField.columnName().length() > 0)) {
					idColumn = databaseField.columnName();
				}
				break;
			}
		}
		return idColumn;
	}

	public JdbcConnectionSource getConnectionSource() {
		return connectionSource;
	}

	@Override
	public void doInTransaction(Callable<?> callable) {
		try {
			TransactionManager.callInTransaction(connectionSource, callable);
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public PagedResponse<T> findByPatternPaged(List<Pair<String, Object>> pattern, long from, long pageSize) {

		final PagedResponse<T> response = new PagedResponse<>();

		QueryBuilder<T, Long> builder = dao.queryBuilder();

		try {
			if ((pattern != null) && !pattern.isEmpty()) {
				Where<T, Long> where = builder.where();
				where = where.eq(pattern.get(0).getKey(), pattern.get(0).getValue());
				for (int i = 1; i < pattern.size(); i++) {
					final String columnName = pattern.get(i).getKey();
					final Object value = pattern.get(i).getValue();
					where = where.and().eq(columnName, value);
				}
			}

			// count
			final long maxEntriesFound = builder.countOf();
			response.setMaxEntriesFound(maxEntriesFound);

			// select
			builder = builder.setCountOf(false);
			final long offset = from * pageSize;
			builder = builder.offset(offset);
			final long pageLimit = pageSize;
			builder = builder.limit(pageLimit);

			builder = builder.orderBy(getIdColumn(), true);
			final PreparedQuery<T> query = builder.prepare();

			LOG.info("query:" + query.getStatement());
			final long start = System.currentTimeMillis();

			// 1.
			List<T> list = new ArrayList<>();
			list = dao.query(query);

			// 2.
			// dao.executeRaw(query.getStatement());

			// 3.

			// List<T> list = new ArrayList<>();
			// CloseableIterator<T> iter = dao.iterator(query);
			// DatabaseResults results = iter.getRawResults();
			// while(iter.hasNext()){
			// T item = iter.next();
			// list.add(item);
			// }
			// iter.close();
			//
			//

			final long time = System.currentTimeMillis() - start;
			LOG.debug("query time:" + time);
			response.setResultList(list);

		} catch (final SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		return response;
	}

	public void enableCache() {
		try {
			dao.setObjectCache(true);
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
