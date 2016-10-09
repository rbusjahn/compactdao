package org.rbusjahn.compactdao;

import java.util.List;

import org.apache.log4j.Logger;

import javafx.util.Pair;

public class GenericDaoStat<T> extends GenericDao<T> {

	private final Logger LOG = Logger.getLogger(getClass());
	private final DatabaseStatDao statDao;

	public GenericDaoStat(Class<T> t, ConnectionSettings settings) {
		super(t, settings);
		statDao = new DatabaseStatDao(settings);
		statDao.createTable();
	}

	public GenericDaoStat(Class<T> t) {
		super(t);
		statDao = new DatabaseStatDao();
		statDao.createTable();
	}

	@Override
	public PagedResponse<T> findByPatternPaged(List<Pair<String, Object>> pattern, long from, long pageSize) {
		final long start = System.currentTimeMillis();
		final PagedResponse<T> response = super.findByPatternPaged(pattern, from, pageSize);
		final long time = System.currentTimeMillis() - start;
		logExecutionTime(time, "findByPatternPaged");
		return response;
	}

	protected void logExecutionTime(long time, String method) {
		final String className = super.clazz.getName();
		LOG.info("execution time: " + className + "." + method + ":" + time);
		final DatabaseStat stat = new DatabaseStat();
		stat.setClassName(super.clazz.getName());
		stat.setExecutionTime(time);
		stat.setTimeStamp(System.currentTimeMillis());
		stat.setMethodName(method);
		this.statDao.save(stat);
	}

}
