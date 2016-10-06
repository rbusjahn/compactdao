package org.rbusjahn.compactdao;

public class DaoEnvironment {

	private static Boolean useInMemoryDatabase = null;

	public static void enableInMemoryDatabase() {
		useInMemoryDatabase = true;
	}

	public static void disableInMemoryDatabase() {
		useInMemoryDatabase = false;
	}

	public static void setUseInMemoryDatabase(boolean enableInMemoryDatabase) {
		useInMemoryDatabase = enableInMemoryDatabase;
	}

	public static boolean useInMemoryDatabase() {
		boolean result = false;
		if (useInMemoryDatabase != null) {
			result = useInMemoryDatabase;
		}
		return result;
	}

}
