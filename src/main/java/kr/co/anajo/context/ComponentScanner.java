package kr.co.anajo.context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.anajo.context.annotation.Component;
import kr.co.anajo.context.annotation.Initialize;
import kr.co.anajo.context.annotation.RequestHandle;

public class ComponentScanner {

	private static final Logger logger = LoggerFactory.getLogger(ComponentScanner.class);

	private String basePackage = "/";

	private final String regexClass = "^(.+)(\\.class)$";

	public ComponentScanner(String basePackage) {
		this.basePackage = basePackage;
	}

	public ComponentDefinitions scan() throws IOException, URISyntaxException {
		ComponentDefinitions componentDefinitions = new ComponentDefinitions();
		Enumeration<URL> resources = ComponentScanner.class.getClassLoader()
				.getResources(this.basePackage.replace(".", "/"));
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			System.out.println("########## " + url);
			if (url.getProtocol().equals("jar")) {
				
			} else {
				File dir = new File(url.toURI());
				visitClass(dir, componentDefinitions);
			}
		}
		return componentDefinitions;
	}

	private void visitClass(File dir, ComponentDefinitions componentDefinitions) {
		if (dir.isFile()) {
			if (dir.getAbsolutePath().matches(regexClass)) {
				String className = dir.getAbsolutePath().replace(File.separator, ".");
				className = className.substring(className.indexOf(this.basePackage), className.lastIndexOf(".class"));
				try {
					Class<?> klass = getClass().getClassLoader().loadClass(className);
					if (klass.isAnnotationPresent(Component.class)) {
						componentDefinitions.add(klass.getSimpleName(), klass);
						for (Method method : klass.getDeclaredMethods()) {
							if (method.isAnnotationPresent(Initialize.class)) {
								componentDefinitions
										.addInitializeMethod(klass.getSimpleName() + "." + method.getName());
							}
							if (method.isAnnotationPresent(RequestHandle.class)) {
								RequestHandle requestHandle = method.getDeclaredAnnotation(RequestHandle.class);
								componentDefinitions.addUrlHandler(requestHandle.url(),
										klass.getSimpleName() + "." + method.getName());
							}
						}
					}
				} catch (ClassNotFoundException e) {
					logger.error("class not found.", e);
				}
			}
		} else {
			Arrays.stream(dir.listFiles()).forEach((t) -> visitClass(t, componentDefinitions));
		}
	}
}
