package org.rbusjahn.compactdao.example.test;

import org.junit.Test;
import org.rbusjahn.compactdao.DaoEnvironment;
import org.rbusjahn.compactdao.example.app.AccountModelDao;

public class LoggingTest {

	@Test
	public void test(){
		DaoEnvironment.enableInMemoryDatabase();
		
		AccountModelDao cut = new AccountModelDao();
		
		cut.createTable();
		
		
	}
}
