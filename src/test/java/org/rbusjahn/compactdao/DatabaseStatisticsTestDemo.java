package org.rbusjahn.compactdao;

import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;
import org.rbusjahn.compactdao.example.app.AccountModel;
import org.rbusjahn.compactdao.example.app.AccountModelDao;


public class DatabaseStatisticsTestDemo {

	private DatabaseStatistics cut;
	private AccountModelDao dao;
	
	@Before
	public void setup(){
		cut = new DatabaseStatistics();
		dao = new AccountModelDao();
	}
	
	@Test
	public void testLogExecutionTime(){
		final AccountModel model = new AccountModel();
		model.setDescription("123");
		
		AccountModel modelSaved = cut.logExecutionTime(new Callable<AccountModel>() {

			@Override
			public AccountModel call() throws Exception {
				dao.save(model);
				return model;
			}
			
		});
		org.junit.Assert.assertNotNull(modelSaved.getId());
		
	}
	@Test
	public void test() throws InterruptedException{
		String token = cut.startMethod(getClass(), "test");
		//start
		Thread.sleep(100);
		//end
		cut.endMethod(token);
		
	}
}
