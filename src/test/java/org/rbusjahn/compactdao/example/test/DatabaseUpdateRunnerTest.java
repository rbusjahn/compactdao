package org.rbusjahn.compactdao.example.test;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.rbusjahn.compactdao.DaoEnvironment;
import org.rbusjahn.compactdao.DatabaseUpdate;
import org.rbusjahn.compactdao.DatabaseUpdateRunner;
import org.rbusjahn.compactdao.example.app.PersonModelDao;

import com.j256.ormlite.support.DatabaseConnection;

public class DatabaseUpdateRunnerTest {

	private final Logger LOG = Logger.getLogger(getClass());

	@Test
	public void testUpdates() {
		final PersonModelDao dao = new PersonModelDao();
		dao.createTable();

		final DatabaseUpdateRunner cut = new DatabaseUpdateRunner();

		final List<DatabaseUpdate> updates = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			final DatabaseUpdate db01 = new DatabaseUpdate();
			db01.setUpdateCommand("alter table person add column number_" + i + " int");
			db01.setDescription("number added");
			db01.setId(Long.valueOf(i));
			updates.add(db01);

		}

		cut.runDatabaseUpdates(updates);

	}

	@Test
	public void testVerifyUpdates() throws SQLException {
		DaoEnvironment.enableInMemoryDatabase();

		final DatabaseUpdateRunner cut = new DatabaseUpdateRunner();
		final List<DatabaseUpdate> updates = new ArrayList<>();

		final DatabaseUpdate createDemoTable = new DatabaseUpdate();
		createDemoTable.setUpdateCommand("CREATE TABLE \"DEMO\" (id BIGINT)");
		createDemoTable.setId(1L);
		updates.add(createDemoTable);

		final DatabaseUpdate alterDemoTable = new DatabaseUpdate();
		alterDemoTable.setUpdateCommand("ALTER TABLE \"DEMO\" ADD COLUMN \"description\" VARCHAR(50) ");
		alterDemoTable.setId(2L);
		updates.add(alterDemoTable);

		final DatabaseUpdate borkenAlterDemoTable = new DatabaseUpdate();
		borkenAlterDemoTable.setUpdateCommand("ALTER TABLE \"DEMO\" ADD COLUMN \"description\" VARCHAR(50) ");
		borkenAlterDemoTable.setId(3L);
		updates.add(borkenAlterDemoTable);

		final List<DatabaseUpdate> brokenUpdates = new ArrayList<>();

		cut.verifyDatabaseUpdates(updates, brokenUpdates);

		Assert.assertEquals(1, brokenUpdates.size());

	}

	@Test
	public void testCreateTableWithRollback() throws SQLException {
		DaoEnvironment.enableInMemoryDatabase();

		final PersonModelDao dao = new PersonModelDao();

		dao.createTable();

		final DatabaseConnection connection = dao.getConnectionSource().getReadWriteConnection();

		connection.setAutoCommit(false);

		final Savepoint sp = connection.setSavePoint("before_ddl");

		try {

			final String statementStr = "create table \"DemoEntity\" (\"id\" BIGINT)";
			final int resultFlags = DatabaseConnection.DEFAULT_RESULT_FLAGS;
			final int result = connection.executeStatement(statementStr, resultFlags);
			LOG.debug("result: " + result);

		} catch (final Exception e) {
			e.printStackTrace();
		}

		connection.rollback(sp);
		// connection.commit(sp);

		try {
			final DatabaseConnection conn2 = dao.getConnectionSource().getReadWriteConnection();
			final boolean tableExists = conn2.isTableExists("DemoEntity");
			LOG.debug("table exists: " + tableExists);
			Assert.assertFalse(tableExists);
		} catch (final Exception e) {
			LOG.error(e.getMessage());
		}

	}
}
