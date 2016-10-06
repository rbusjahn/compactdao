package org.rbusjahn.compactdao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "database_stat")
public class DatabaseStat {
	@DatabaseField(generatedId=true)
	private Long id;
	@DatabaseField()
	private String className;
	@DatabaseField() 
	private String methodName;
	@DatabaseField()
	private long executionTime;
	@DatabaseField()
	private long timeStamp;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
