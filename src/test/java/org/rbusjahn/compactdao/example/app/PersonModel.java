package org.rbusjahn.compactdao.example.app;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "person")
public class PersonModel {

	@DatabaseField(generatedId = true)
	private Long id;

	private Long version;
	@DatabaseField()
	private String firstname;
	@DatabaseField()
	private String lastname;
	@DatabaseField(foreign = true)
	private AccountModel fkAccount;
	// @DatabaseField
	// private Integer number1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
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

	public AccountModel getFkAccount() {
		return fkAccount;
	}

	public void setFkAccount(AccountModel fkAccount) {
		this.fkAccount = fkAccount;
	}

	// public Integer getNumber1() {
	// return number1;
	// }
	//
	// public void setNumber1(Integer number1) {
	// this.number1 = number1;
	// }

}
