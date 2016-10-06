package org.rbusjahn.compactdao.cucumber.testmodel;

import org.rbusjahn.compactdao.GenericDao;

public class PersonDao extends GenericDao<PersonEntity> {

	public PersonDao() {
		super(PersonEntity.class);
	}

}
