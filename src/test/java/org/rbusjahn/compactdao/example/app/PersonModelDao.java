package org.rbusjahn.compactdao.example.app;

import org.rbusjahn.compactdao.ConnectionSettings;
import org.rbusjahn.compactdao.GenericDao;

public class PersonModelDao extends GenericDao<PersonModel> {

	public PersonModelDao(ConnectionSettings settings) {
		super(PersonModel.class, settings);
	}

	public PersonModelDao() {
		super(PersonModel.class);
	}

}
