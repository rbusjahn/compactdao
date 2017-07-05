package org.rbusjahn.compactdao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabaseUpdateRunnerTest {

	private DatabaseUpdateRunner cut;

	@Before
	public void setup() {
		DaoEnvironment.enableInMemoryDatabase();
		cut = new DatabaseUpdateRunner();
	}

	@Test
	public void testGetOpenUpdates() {
		// GIVEN
		DatabaseUpdate update1 = new DatabaseUpdate();
		update1.setId(1L);
		DatabaseUpdate update2 = new DatabaseUpdate();
		update2.setId(2L);

		List<DatabaseUpdate> appliedUpdates = new ArrayList<>();
		appliedUpdates.add(update1);

		List<DatabaseUpdate> updates = new ArrayList<>();
		updates.add(update1);
		updates.add(update2);

		// WHEN
		List<DatabaseUpdate> result = cut.getOpenUpdates(appliedUpdates, updates);

		// THEN
		Assert.assertEquals(2L, result.get(0).getId().longValue());

	}
}
