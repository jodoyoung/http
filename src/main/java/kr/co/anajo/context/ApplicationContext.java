package kr.co.anajo.context;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.anajo.context.annotation.DI;

public class ApplicationContext {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

	private final Map<String, Object> components = new ConcurrentHashMap<String, Object>(10);

	private ComponentDefinitions componentDefinitions;

	private String basePackage = "/";

	private static class ApplicationContextHolder {
		private static ApplicationContext instance = null;
	}

	public static ApplicationContext getInstance() {
		while (ApplicationContextHolder.instance == null) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ApplicationContextHolder.instance;
	}

	public ApplicationContext(String basePackage) {
		this.basePackage = basePackage;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> type) {
		String beanName = type.getSimpleName();
		Object bean = this.getBean(beanName);
		if (bean == null) {
			bean = this.registBean(beanName);
		}
		if (type.isInstance(bean)) {
			return (T) bean;
		} else {
			throw new IllegalArgumentException(String.format("bean type mismatch! %s != (componet)%s", type, bean));
		}
	}

	public Object getBean(String name) {
		Object bean = this.components.get(name);
		if (bean == null) {
			bean = this.registBean(name);
		}
		return bean;
	}

	public <T> void setBean(Class<T> type) {
		components.put(type.getSimpleName(), type);
	}

	private Object registBean(String name) {
		// TODO anno-config(profile)
		Class<?> klass = this.componentDefinitions.get(name);
		Object bean = null;
		try {
			bean = klass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("register bean failed!", e);
		}

		this.components.put(name, bean);
		for (Field field : klass.getDeclaredFields()) {
			if (field.isAnnotationPresent(DI.class)) {
				String memberClassName = field.getType().getSimpleName();
				Object memberComponent = this.components.get(memberClassName);
				if (memberComponent == null) {
					memberComponent = this.registBean(memberClassName);
				}
				field.setAccessible(true);
				try {
					field.set(bean, memberComponent);
				} catch (Exception e) {
					logger.error("filed mapping failed!", e);
				}
			}
		}
		return bean;
	}

	public void start() {
		try {
			ComponentScanner scanner = new ComponentScanner(this.basePackage);
			this.componentDefinitions = scanner.scan();
			ApplicationContextHolder.instance = this;
			this.initialize();
		} catch (IOException | URISyntaxException e) {
			logger.error("component scan failed!", e);
		}
	}

	public void initialize() {
		List<String> initFunctions = this.componentDefinitions.getInitializeMethods();

		for (String initFunction : initFunctions) {
			String[] initFunc = initFunction.split("\\.");
			Object obj = this.getBean(initFunc[0]);
			Class<?> klass = obj.getClass();
			try {
				Method method = klass.getDeclaredMethod(initFunc[1], null);
				method.invoke(obj, null);
			} catch (NoSuchMethodException | SecurityException e) {
				logger.error("initialize method not found!", e);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				logger.error("initialize method invoke failed!", e);
			}
		}
	}

	public void stop() {
		// TODO database context destroy
	}

	public String getUrlHandler(String uri) {
		return this.componentDefinitions.getUrlHandler(uri);
	}
}