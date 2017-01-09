package org.rbusjahn.compactdao.example.test;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rbusjahn.compactdao.DaoEnvironment;
import org.rbusjahn.compactdao.DatabaseUpdateRunner;
import org.rbusjahn.compactdao.example.app.AccountModel;
import org.rbusjahn.compactdao.example.app.AccountModelDao;
import org.rbusjahn.compactdao.example.app.PersonModel;
import org.rbusjahn.compactdao.example.app.PersonModelDao;

import com.j256.ormlite.support.DatabaseConnection;

public class PersonModelDaoTest {
	private final Logger log = Logger.getLogger(getClass());

	protected PersonModelDao getCut() {
		return new PersonModelDao();
	}

	PersonModelDao cut;
	private DatabaseUpdateRunner databaseUpdate;

	@Before
	public void before() {
		DaoEnvironment.enableInMemoryDatabase();
		cut = new PersonModelDao();
		cut.createTable();
		databaseUpdate = new DatabaseUpdateRunner();
		databaseUpdate.runDatabaseUpdates(null, null);

	}

	@Test
	public void testAddPerson() {

		final int itemsBefore = cut.findAll().size();

		log.debug("items:" + itemsBefore);

		final PersonModel person = new PersonModel();
		cut.save(person);
		Assert.assertNotNull(person.getId());

	}

	@Test
	public void testCrud() throws SQLException {

		cut.createTable();

		final PersonModel person = new PersonModel();
		cut.save(person);
		Assert.assertNotNull(person.getId());

		person.setFirstname("firstname");
		cut.save(person);

		final PersonModel reloaded = cut.getById(person.getId());
		Assert.assertEquals(person.getFirstname(), reloaded.getFirstname());

		final int items = cut.findAll().size();
		log.debug("items:" + items);

	}

	@Test
	public void testAddPersonWithAccount() {
		final AccountModelDao accountDao = new AccountModelDao();
		accountDao.createTable();
		final AccountModel account = new AccountModel();
		account.setNumber("001122");
		accountDao.save(account);
		Assert.assertNotNull(account);

		final PersonModel person = new PersonModel();
		person.setFirstname("Mike");
		person.setLastname("Miller");
		person.setFkAccount(account);

		cut.save(person);
		Assert.assertNotNull(person.getId());

	}

	@Ignore
	@Test
	public void testUpdatePerson() {
		try {

			final DatabaseConnection connection = cut.getConnectionSource().getReadWriteConnection();
			final String sql = "alter table person add column number1 integer";
			connection.executeStatement(sql, DatabaseConnection.DEFAULT_RESULT_FLAGS);
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
}
