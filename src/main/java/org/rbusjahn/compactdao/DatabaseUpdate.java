package org.rbusjahn.compactdao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "database_update")
public class DatabaseUpdate {

	@DatabaseField(generatedId = true)
	private Long id;
	@DatabaseField
	private Long versionNumber;
	@DatabaseField
	private String updateCommand;
	@DatabaseField
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUpdateCommand() {
		return updateCommand;
	}

	public void setUpdateCommand(String updateCommand) {
		this.updateCommand = updateCommand;
	}

	@Override
	public String toString() {
		return "DatabaseUpdate [" + (id != null ? "id=" + id + ", " : "")
				+ (versionNumber != null ? "versionNumber=" + versionNumber + ", " : "")
				+ (updateCommand != null ? "updateCommand=" + updateCommand + ", " : "")
				+ (description != null ? "description=" + description : "") + "]";
	}

}
