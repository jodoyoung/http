package kr.co.anajo.context;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import kr.co.anajo.context.annotation.DI;

public class ApplicationContext {

	protected final Logger logger = Logger.getLogger(ApplicationContext.class.getName());

	private final Map<String, Object> components = new ConcurrentHashMap<String, Object>(10);

	private Map<String, Class<?>> beanDefinitions;

	private String basePackage = "/";

	public ApplicationContext(String basePackage) {
		this.basePackage = basePackage;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> type) {
		String beanName = type.getSimpleName();
		Object bean = this.components.get(beanName);
		if (bean == null) {
			bean = this.registBean(beanName);
		}
		if (type.isInstance(bean)) {
			return (T) bean;
		} else {
			throw new IllegalArgumentException(String.format("bean type mismatch! %s != (componet)%s", type, bean));
		}
	}

	private Object registBean(String name) {
		// TODO anno-config(profile), anno-controller, di(proxy)
		Class<?> klass = this.beanDefinitions.get(name);
		Object bean = null;
		try {
			bean = klass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.severe(() -> String.format("register bean failed! %s", e));
		}

		this.components.put(name, bean);
		for (Field field : klass.getDeclaredFields()) {
			if (field.getAnnotation(DI.class) != null) {
				String memberClassName = field.getType().getSimpleName();
				Object memberComponent = this.components.get(memberClassName);
				if (memberComponent == null) {
					memberComponent = this.registBean(memberClassName);
				}
				field.setAccessible(true);
				try {
					field.set(bean, memberComponent);
				} catch (Exception e) {
					logger.severe(() -> String.format("filed mapping failed! %s", e));
				}
			}
		}
		return bean;
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
		} catch (IOException | URISyntaxException e) {
			logger.severe(() -> String.format("component scan failed! %s", e));
		}
	}

	public void stop() {
		// TODO database context destroy
	}
}