package org.rbusjahn.compactdao;

public class DatabaseStatDao extends GenericDao<DatabaseStat>{

	public DatabaseStatDao(ConnectionSettings settings) {
		super(DatabaseStat.class, settings);
	}
	
	public DatabaseStatDao() {
		super(DatabaseStat.class);
	}

	
}
