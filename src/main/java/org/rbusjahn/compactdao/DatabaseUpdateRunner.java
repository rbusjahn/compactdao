package org.rbusjahn.compactdao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.j256.ormlite.support.DatabaseConnection;

public class DatabaseUpdateRunner {

	private final Logger log = Logger.getLogger(getClass());
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

		final List<DatabaseUpdate> appliedUpdates = dao.findAll();
		List<DatabaseUpdate> openUpdates = getOpenUpdates(appliedUpdates, updates);
		if (openUpdates.size() > 0) {

			final DatabaseConnection connection = dao.getConnectionSource().getReadWriteConnection();

			connection.setAutoCommit(false);

			final Savepoint sp = connection.setSavePoint("before_verify_database_updates");

			for (DatabaseUpdate update : openUpdates) {
				try {
					log.debug("testing update: " + update);
					final int resultFlags = DatabaseConnection.DEFAULT_RESULT_FLAGS;
					final int result = connection.executeStatement(update.getUpdateCommand(), resultFlags);
					log.debug("result: " + result);
				} catch (final Exception e) {
					log.error(e.getMessage(), e);
					brokenUpdates.add(update);
				}
			}

			connection.rollback(sp);
			connection.closeQuietly();
		}

	}

	protected List<DatabaseUpdate> getOpenUpdates(List<DatabaseUpdate> appliedUpdates, List<DatabaseUpdate> updates) {
		List<DatabaseUpdate> list = new ArrayList<>();
		for (DatabaseUpdate newUpdate : updates) {
			boolean foundById = false;
			for (DatabaseUpdate existingUpdate : appliedUpdates) {
				if (newUpdate.getId().equals(existingUpdate.getId())) {
					foundById = true;
					break;
				}
			}
			if (!foundById) {
				list.add(newUpdate);
			}
		}
		return list;
	}

	public void runDatabaseUpdates(List<DatabaseUpdate> updates, List<DatabaseUpdate> failedUpdates) {
		if (updates == null || updates.isEmpty()) {
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

				try {
					log.debug("running update: " + update);

					dao.runUpdateSql(update.getUpdateCommand());

					dao.save(update);

				} catch (final Exception e) {
					log.error(e.getMessage(), e);
					failedUpdates.add(update);
				}
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
