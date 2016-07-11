package kr.co.anajo.context;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import kr.co.anajo.context.annotation.Component;

public class ComponentScanner {

	private final Logger logger = Logger.getLogger(ComponentScanner.class.getName());

	private String basePackage = "/";

	private final String regexClass = "^(.+)(\\.class)$";

	public ComponentScanner(String basePackage) {
		this.basePackage = basePackage;
	}

	public Map<String, Class<?>> scan() throws IOException, URISyntaxException {
		Map<String, Class<?>> beanDefinitions = new HashMap<String, Class<?>>(10);
		Enumeration<URL> resources = ComponentScanner.class.getClassLoader()
				.getResources(this.basePackage.replace(".", "/"));
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			File dir = new File(url.toURI());
			visitClass(dir, beanDefinitions);
		}
		return beanDefinitions;
	}

	private void visitClass(File dir, Map<String, Class<?>> beanDefinitions) {
		if (dir.isFile()) {
			if (dir.getAbsolutePath().matches(regexClass)) {
				String className = dir.getAbsolutePath().replace(File.separator, ".");
				className = className.substring(className.indexOf(this.basePackage), className.lastIndexOf(".class"));
				try {
					Class<?> klass = getClass().getClassLoader().loadClass(className);
					if (klass.isAnnotationPresent(Component.class)) {
						beanDefinitions.put(klass.getSimpleName(), klass);
					}
				} catch (ClassNotFoundException e) {
					logger.severe(() -> String.format("class not found. %d", e));
				}
			}
		} else {
			Arrays.stream(dir.listFiles()).forEach((t) -> visitClass(t, beanDefinitions));
		}
	}
}
