package org.rbusjahn.compactdao.example.test;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rbusjahn.compactdao.DaoEnvironment;
import org.rbusjahn.compactdao.example.app.AppDatabaseUpdate;

@Ignore
public class AppDatabaseUpdateDemo {

	@Before
	public void setup() {
		DaoEnvironment.enableInMemoryDatabase();
	}

	@Test
	public void testOnApplicationStart() {
		final AppDatabaseUpdate cut = new AppDatabaseUpdate();
		cut.onApplicationStart();
	}
}
