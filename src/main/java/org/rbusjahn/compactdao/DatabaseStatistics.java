package org.rbusjahn.compactdao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

public class DatabaseStatistics {
	private Logger log = Logger.getLogger(getClass());

	private DatabaseStatDao dao = new DatabaseStatDao();
	private Map<String, Long> executionTimes = new HashMap<>();

	public String startMethod(Class<?> c, String method) {
		String key = c.getCanonicalName() + "." + method;
		Long now = getNow();
		executionTimes.put(key, now);
		return key;
	}

	public void endMethod(String method) {
		Long time = executionTimes.get(method);
		if (time != null) {

		}

	}

	public <T> T logExecutionTime(Callable<T> task) {
		StackTraceElement stackTraceElement = new Exception().getStackTrace()[1];
		String className = stackTraceElement.getClassName();
		String methodName = stackTraceElement.getMethodName();
		log.info("classname:" + className);
		log.info("method:" + methodName);
		Long start = System.currentTimeMillis();
		T result = null;
		try {
			result = task.call();
			Long time = System.currentTimeMillis() - start;
			log.info("time:" + time);
			DatabaseStat stat = new DatabaseStat();
			stat.setClassName(className);
			stat.setMethodName(methodName);
			stat.setExecutionTime(time);
			stat.setTimeStamp(start);
			dao.save(stat);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}

	private Long getNow() {
		Long now = System.nanoTime();
		return now;
	}

	public void setDao(DatabaseStatDao dao) {
		this.dao = dao;
	}

}
