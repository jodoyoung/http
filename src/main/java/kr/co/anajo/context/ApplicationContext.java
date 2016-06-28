package kr.co.anajo.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

	private static final Map<String, Object> components = new ConcurrentHashMap<String, Object>();

	public static Object getBean(String name) {
		return components.get(name);
	}

	public static <T> void setBean(Class<T> type) {
		components.put(type.getSimpleName(), type);
	}

}