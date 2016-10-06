package org.rbusjahn.compactdao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.j256.ormlite.support.DatabaseConnection;

public class DatabaseUpdateRunner {

	private final Logger LOG = Logger.getLogger(getClass());
	private DatabaseVersionDao dao;

	public DatabaseUpdateRunner() {
		init();
	}

	protected void init() {
		dao = new DatabaseVersionDao();
		dao.createTable();
	}

	public void verifyDatabaseUpdates(List<DatabaseUpdate> updates, List<DatabaseUpdate> brokenUpdates)
			throws SQLException {
		Collections.sort(updates, new Comparator<DatabaseUpdate>() {
			@Override
			public int compare(DatabaseUpdate o1, DatabaseUpdate o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		final List<DatabaseUpdate> appliedUpdates = dao.findAll();
		final int updatesLeft = updates.size() - appliedUpdates.size();
		if (updatesLeft > 0) {
			final int offset = updates.size() - updatesLeft;

			final DatabaseConnection connection = dao.getConnectionSource().getReadWriteConnection();

			connection.setAutoCommit(false);

			final Savepoint sp = connection.setSavePoint("before_verify_database_updates");

			for (int i = offset; i < updates.size(); i++) {
				final DatabaseUpdate update = updates.get(i);
				try {
					LOG.debug("running update: " + update);
					final int resultFlags = DatabaseConnection.DEFAULT_RESULT_FLAGS;
					final int result = connection.executeStatement(update.getUpdateCommand(), resultFlags);
					LOG.debug("result: " + result);
				} catch (final Exception e) {
					LOG.error(e.getMessage(), e);
					brokenUpdates.add(update);
				}
			}

			connection.rollback(sp);
			connection.closeQuietly();
		}

	}

	public void runDatabaseUpdates(List<DatabaseUpdate> updates) {
		if ((updates == null) || updates.isEmpty()) {
			return;
		}
		Collections.sort(updates, new Comparator<DatabaseUpdate>() {
			@Override
			public int compare(DatabaseUpdate o1, DatabaseUpdate o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});

		final List<DatabaseUpdate> appliedUpdates = dao.findAll();
		final int updatesLeft = updates.size() - appliedUpdates.size();
		if (updatesLeft > 0) {
			final int offset = updates.size() - updatesLeft;
			for (int i = offset; i < updates.size(); i++) {
				final DatabaseUpdate update = updates.get(i);
				LOG.debug("running update: " + update);
				final Callable<Void> task = new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						dao.runUpdateSql(update.getUpdateCommand());
						dao.save(update);
						return null;
					}
				};

				dao.doInTransaction(task);

			}
		}

	}

	public void startUpdateProcess(List<DatabaseUpdate> updates, List<DatabaseUpdate> failedUpdates) {
		try {
			verifyDatabaseUpdates(updates, failedUpdates);
			if (failedUpdates.isEmpty()) {
				runDatabaseUpdates(updates);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}
}
