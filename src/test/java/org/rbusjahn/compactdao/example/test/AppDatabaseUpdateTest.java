package org.rbusjahn.compactdao.example.test;

import org.junit.Before;
import org.junit.Test;
import org.rbusjahn.compactdao.DaoEnvironment;
import org.rbusjahn.compactdao.example.app.AppDatabaseUpdate;

public class AppDatabaseUpdateTest {
	
	@Before
	public void setup(){
		DaoEnvironment.enableInMemoryDatabase();
	}

	@Test
	public void testOnApplicationStart() {
		final AppDatabaseUpdate cut = new AppDatabaseUpdate();
		cut.onApplicationStart();
	}
}
