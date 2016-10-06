package org.rbusjahn.compactdao;

public class DefaultConnectionSettings implements ConnectionSettings{

	@Override
	public String getDatabaseFolderName() {
		return "DerbyDB";
	}

	@Override
	public String getDerbyConnectionParameter() {
		return "create=true";
	}

}
