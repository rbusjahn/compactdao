package org.rbusjahn.compactdao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
					LOG.debug("testing update: " + update);
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

	public void runDatabaseUpdates(List<DatabaseUpdate> updates, List<DatabaseUpdate> failedUpdates) {
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

				DatabaseConnection connection = null;

				try {

					// connection =
					// dao.getConnectionSource().getReadWriteConnection();
					// connection.setAutoCommit(false);

					// final Savepoint sp =
					// connection.setSavePoint("before_update_database_updates");

					try {
						LOG.debug("running update: " + update);
						// final int resultFlags =
						// DatabaseConnection.DEFAULT_RESULT_FLAGS;
						// final int result =
						// connection.executeStatement(update.getUpdateCommand(),
						// resultFlags);
						// LOG.debug("result: " + result);

						dao.runUpdateSql(update.getUpdateCommand());

						dao.save(update);

						// connection.commit(sp);
					} catch (final Exception e) {
						LOG.error(e.getMessage(), e);
						failedUpdates.add(update);
						// connection.rollback(sp);
					}


				} catch (Exception e) {
					LOG.error(e.getCause(), e);
					// closeConnection(connection);
				}
			}
		}

	}

	private void closeConnection(DatabaseConnection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e2) {
				LOG.error(e2.getMessage(), e2);
			}
		}
	}

	public void startUpdateProcess(List<DatabaseUpdate> updates, List<DatabaseUpdate> failedUpdates) {
		try {
			verifyDatabaseUpdates(updates, failedUpdates);
			if (failedUpdates.isEmpty()) {
				runDatabaseUpdates(updates, failedUpdates);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}
}
