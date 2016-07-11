package kr.co.anajo.context;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.mongodb.bulk.WriteRequest.Type;

public class ApplicationContext {

	protected final Logger logger = Logger.getLogger(ApplicationContext.class.getName());

	private final Map<String, Object> components = new ConcurrentHashMap<String, Object>(10);

	private Map<String, Class<?>> beanDefinitions;

	private String basePackage = "/";

	public ApplicationContext(String basePackage) {
		this.basePackage = basePackage;
	}

	public <T> T getBean(String name, Class<T> type) {
		Object bean = this.getBean(name);
		// TODO
		return null;
	}

	public Object getBean(String name) {
		Object bean = this.components.get(name);
		if (bean != null) {
			return bean;
		}

		// TODO anno-config(profile), anno-controller, di(proxy)
		return components.get(name);
	}

	private Object registerBean(String name) {
		Class<?> klass = this.beanDefinitions.get(name);
		Object bean = null;
		try {
			bean = klass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.severe(() -> String.format("register bean failed! %d", e));
		}

		return null;
	}

	public <T> void setBean(Class<T> type) {
		components.put(type.getSimpleName(), type);
	}

	public Class<?> getBeanDefinition(String name) {
		return beanDefinitions.get(name);
	}

	public void start() {
		try {
			ComponentScanner scanner = new ComponentScanner(this.basePackage);
			beanDefinitions = scanner.scan();
			System.out.println(beanDefinitions);
		} catch (IOException | URISyntaxException e) {
			logger.severe(() -> String.format("component scan failed! %d", e));
		}
	}

	public void stop() {
		// TODO database context destroy
	}
}