package org.rbusjahn.compactdao.example.app;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "account")
public class AccountModel {
	@DatabaseField(generatedId=true)
	private Long id;
	private Long version;
	@DatabaseField()
	private String number;
	@DatabaseField()
	private String description;
	
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
