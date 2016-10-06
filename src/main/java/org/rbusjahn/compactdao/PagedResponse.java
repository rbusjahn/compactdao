package org.rbusjahn.compactdao;

import java.util.List;

public class PagedResponse <T>{

	private long maxEntriesFound;
	private List<T> resultList;
	public long getMaxEntriesFound() {
		return maxEntriesFound;
	}
	public void setMaxEntriesFound(long maxEntriesFound) {
		this.maxEntriesFound = maxEntriesFound;
	}
	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	
	
	
	
}
