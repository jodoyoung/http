package kr.co.anajo.http;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;

import org.junit.Test;

public class ClassLoaderTest {

	private String regex = "^(.+)(\\.class)$";

	@Test
	public void loadResources() throws Exception {
		String basePackage = "kr.co.anajo";

		Enumeration<URL> resources = getClass().getClassLoader()
				.getResources(basePackage.replace(".", "/"));
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			File dir = new File(url.toURI());
			visitClass(dir);
		}
	}

	public void visitClass() throws Exception {
		Path path = Paths.get("D:\\workspace");
		visitClass(path.toFile());
	}

	private void visitClass(File dir) {
		if (dir.isFile()) {
			if (dir.toString().matches(regex)) {
				try {
					String classFilePullPath = dir.getAbsolutePath();
					String className = classFilePullPath.substring(classFilePullPath.indexOf("kr.co.anajo"), classFilePullPath.lastIndexOf(".class"));
					System.out.println("############## class file : " + className);
					Class klass = getClass().getClassLoader().loadClass("kr.co.anajo.Main");
					System.out.println("############## class : " + klass.getName());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else {
			Arrays.stream(dir.listFiles()).forEach((t) -> visitClass(t));
		}
	}

}