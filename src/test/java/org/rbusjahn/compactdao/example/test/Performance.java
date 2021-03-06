package org.rbusjahn.compactdao.example.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.rbusjahn.compactdao.example.app.AccountModel;
import org.rbusjahn.compactdao.example.app.AccountModelDao;

public class Performance {

	private Logger log = Logger.getLogger(getClass());

	private AccountModelDao cut;

	@Before
	public void setup() {
		cut = new AccountModelDao();

		cut.createTable();
	}

	@Test
	public void testWrite() {
		int N = 1000 * 10;

		List<AccountModel> list = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			AccountModel model = new AccountModel();
			model.setDescription(getTestData());
			model.setNumber(getTestData());
			list.add(model);
		}

		long start = System.currentTimeMillis();

		cut.save(list);

		long time = System.currentTimeMillis() - start;
		log.info("time:" + time);

	}

	@Test
	public void testRead() throws SQLException, InterruptedException, ExecutionException {

		ExecutorService service = Executors.newFixedThreadPool(5);
		List<Future<Long>> tasks = new ArrayList<>();
		new Random();
		final int[] counter = new int[] { 0 };

		int M = 10;

		for (int i = 0; i < M; i++) {

			Future<Long> task = service.submit(new Callable<Long>() {

				@Override
				public Long call() throws Exception {
					long start = System.currentTimeMillis();
					int a = 100 + counter[0]++;
					int b = 25 + counter[0]++;
					cut.findByPatternPaged(null, a, b).getResultList();
					long time = System.currentTimeMillis() - start;
					return time;
				}
			});
			tasks.add(task);
		}

		Set<Long> times = new HashSet<>();
		for (Future<Long> t : tasks) {
			times.add(t.get());
		}
		Long[] timeList = new Long[times.size()];
		times.toArray(timeList);
		List<Long> timeStats = Arrays.asList(timeList);
		Collections.sort(timeStats);
		for (Long t : timeStats) {
			log.info("time:" + t);
		}

	}

	private String getTestData() {
		return "" + System.currentTimeMillis();
	}
}
