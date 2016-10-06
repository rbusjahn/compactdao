package org.rbusjahn.compactdao;

public class InMemoryConnectionSettings implements ConnectionSettings{

	@Override
	public String getDatabaseFolderName() {
		return "memory:DerbyDB";
	}

	@Override
	public String getDerbyConnectionParameter() {
		return "create=true";
	}

}
