package kr.co.anajo.context;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

import kr.co.anajo.context.annotation.Component;

public class ComponentScanner {

	private String basePackage = "/";

	private final String regexClass = "^(.+)(\\.class)$";

	public ComponentScanner(String basePackage) {
		this.basePackage = basePackage;
	}

	public void scan() throws Exception {
		Enumeration<URL> resources = ComponentScanner.class.getClassLoader()
				.getResources(this.basePackage.replace(".", "/"));
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			File dir = new File(url.toURI());
			visitClass(dir);
		}
	}

	private void visitClass(File dir) {
		if (dir.isFile()) {
			if (dir.getAbsolutePath().matches(regexClass)) {
				String className = dir.getAbsolutePath().replace(File.separator, ".");
				className = className.substring(className.indexOf(this.basePackage), className.lastIndexOf(".class"));
				try {
					Class<?> klass = getClass().getClassLoader().loadClass(className);
					if(klass.isAnnotationPresent(Component.class)) {
						System.out.println("#### " + klass);
						// TODO anno-config(profile), anno-cnotroller, di(proxy)
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else {
			Arrays.stream(dir.listFiles()).forEach((t) -> visitClass(t));
		}
	}
}
