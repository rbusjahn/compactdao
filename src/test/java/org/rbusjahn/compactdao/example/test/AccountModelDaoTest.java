package org.rbusjahn.compactdao.example.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rbusjahn.compactdao.DaoEnvironment;
import org.rbusjahn.compactdao.PagedResponse;
import org.rbusjahn.compactdao.example.app.AccountModel;
import org.rbusjahn.compactdao.example.app.AccountModelDao;

import javafx.util.Pair;

public class AccountModelDaoTest {

	private final Logger LOG = Logger.getLogger(getClass());

	private AccountModelDao cut;

	@Before
	public void setup() {
		DaoEnvironment.enableInMemoryDatabase();
		cut = new AccountModelDao();
		cut.createTable();
	}


	@Test
	public void testCrud() {
		final AccountModel account = new AccountModel();
		account.setNumber("123");
		cut.save(account);
		Assert.assertNotNull(account.getId());

		account.setNumber("123");
		cut.save(account);

		final AccountModel updatedAccount = cut.getById(account.getId());
		Assert.assertEquals(account.getId(), updatedAccount.getId());
		Assert.assertEquals("123", updatedAccount.getNumber());

		cut.delete(updatedAccount);
		Assert.assertNull(cut.getById(updatedAccount.getId()));

	}

	@Test
	public void testFindByPattern() throws SQLException {
		//GIVEN
		addSomeTestAccounts();

		final List<Pair<String, Object>> pattern = new ArrayList<>();
		pattern.add(new Pair<String, Object>("number", "NumberB"));

		LOG.debug("testFindByPattern");

		//WHEN
		final PagedResponse<AccountModel> result = cut.findByPatternPaged(pattern, 1,5);
		
		//THEN
		Assert.assertNotNull(result);
		Assert.assertEquals(5, result.getResultList().size());
		Assert.assertEquals(20, result.getMaxEntriesFound());
	}

	private void addSomeTestAccounts() {
		for (int i = 0; i < 20; i++) {
			final AccountModel account = new AccountModel();
			account.setNumber("NumberA");
			account.setDescription("description_" + i);
			cut.save(account);
		}
		for (int i = 0; i < 20; i++) {
			final AccountModel account = new AccountModel();
			account.setNumber("NumberB");
			account.setDescription("description_" + i);
			cut.save(account);
		}
	}
}
