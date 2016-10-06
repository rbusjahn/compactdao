package org.rbusjahn.compactdao.cucumber.testmodel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class PersonEntity {

	@DatabaseField(generatedId = true)
	private Long id;
	@DatabaseField()
	private String firstname;
	@DatabaseField()
	private String lastname;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}
