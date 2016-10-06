package org.rbusjahn.compactdao.example.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.rbusjahn.compactdao.ConnectionSettings;
import org.rbusjahn.compactdao.DatabaseUpdate;
import org.rbusjahn.compactdao.DatabaseUpdateRunner;
import org.rbusjahn.compactdao.InMemoryConnectionSettings;

public class AppDatabaseUpdate {

	private final DatabaseUpdateRunner runner = new DatabaseUpdateRunner();

	private final Logger LOG = Logger.getLogger(getClass());

	public void onApplicationStart() {

		final ConnectionSettings settings = new InMemoryConnectionSettings();
		final PersonModelDao personModelDao = new PersonModelDao(settings);
		final AccountModelDao accountModelDao = new AccountModelDao(settings);

		personModelDao.createTable();
		accountModelDao.createTable();

		final List<DatabaseUpdate> failedUpdates = new ArrayList<>();

		runner.startUpdateProcess(getUpdates(), failedUpdates);

		if (!failedUpdates.isEmpty()) {
			for (final DatabaseUpdate updateError : failedUpdates) {
				LOG.error("failed update: " + updateError);
			}
		}
	}

	protected List<DatabaseUpdate> getUpdates() {
		final List<DatabaseUpdate> updates = new ArrayList<>();
		final DatabaseUpdate update1 = new DatabaseUpdate();
		update1.setDescription("extend person table");
		update1.setId(1L);
		update1.setUpdateCommand("123 123 123");
		update1.setVersionNumber(1L);

		updates.add(update1);
		return updates;
	}
}
