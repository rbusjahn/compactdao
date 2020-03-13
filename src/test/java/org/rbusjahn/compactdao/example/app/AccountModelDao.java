package org.rbusjahn.compactdao.example.app;

import org.rbusjahn.compactdao.ConnectionSettings;
import org.rbusjahn.compactdao.GenericDao;
import org.rbusjahn.compactdao.GenericDaoStat;

public class AccountModelDao extends GenericDao<AccountModel> implements IAccountModelDao {


	public AccountModelDao(ConnectionSettings settings) {
		super(AccountModel.class, settings);
	}

	public AccountModelDao() {
		super(AccountModel.class);
		super.createTable();
	}

}
