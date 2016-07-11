package kr.co.anajo.context;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import kr.co.anajo.context.parser.BeanDefinition;

public class ApplicationContext {

	protected final Logger logger = Logger.getLogger(ApplicationContext.class.getName());

	private final Map<String, Object> components = new ConcurrentHashMap<String, Object>(10);

	private final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<String, BeanDefinition>(10);

	private ComponentScanner scanner;

	public ApplicationContext(String basePackage) {
		this.scanner = new ComponentScanner(basePackage);
	}

	public Object getBean(String name) {
		return components.get(name);
	}

	public <T> void setBean(Class<T> type) {
		components.put(type.getSimpleName(), type);
	}

	public BeanDefinition getBeanDefinition(String name) {
		return beanDefinitions.get(name);
	}

	public void setBeanDefinition(BeanDefinition bean) {
		beanDefinitions.put(bean.getName(), bean);
	}

	public void start() {
		try {
			this.scanner.scan();
		} catch (IOException | URISyntaxException e) {
			logger.severe(() -> String.format("component scan failed! %d", e));
		}
	}

	public void stop() {
		// TODO database context destroy
	}
}