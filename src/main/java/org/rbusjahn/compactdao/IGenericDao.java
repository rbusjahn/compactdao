package org.rbusjahn.compactdao;

import java.util.List;
import java.util.concurrent.Callable;

public interface IGenericDao<T> {

	void init(ConnectionSettings settings);

	void save(T t);

	void delete(T t);

	T getById(Long id);

	List<T> getByIdList(List<Long> ids);

	void createTable();

	void save(List<T> list);

	List<T> findAll();

	void doInTransaction(Callable<?> callable);
	
}
