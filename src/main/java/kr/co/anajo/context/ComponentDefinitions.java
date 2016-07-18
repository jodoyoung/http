package kr.co.anajo.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentDefinitions {

	private Map<String, Class<?>> beanDefinitions = new HashMap<>();

	private List<String> initializeMethods = new ArrayList<>();

	private Map<String, String> urlHandlerMappings = new HashMap<>();

	public void add(String name, Class<?> klass) {
		this.beanDefinitions.put(name, klass);
	}

	public Class<?> get(String name) {
		return this.beanDefinitions.get(name);
	}

	public void addInitializeMethod(String method) {
		this.initializeMethods.add(method);
	}

	public List<String> getInitializeMethods() {
		return this.initializeMethods;
	}

	public void addUrlHandler(String url, String handler) {
		this.urlHandlerMappings.put(url, handler);
	}

	public String getUrlHandler(String url) {
		return this.urlHandlerMappings.get(url);
	}
}
