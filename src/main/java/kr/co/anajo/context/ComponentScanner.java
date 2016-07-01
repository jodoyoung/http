package kr.co.anajo.context;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;

public class ComponentScanner {

	private static final String regex = "^(.+)(\\.class)$";

	public static void scan(String basePackage) throws Exception {
		Enumeration<URL> resources = ComponentScanner.class.getClassLoader().getResources("kr/co/anajo");
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			File dir = new File(url.toURI());
			visitClass(dir);
		}
	}

	private static void visitClass(File dir) {
		if (dir.isFile()) {
			if (dir.toString().matches(regex)) {
				System.out.println("##############class file : " + dir);
			}
		} else {
			Arrays.stream(dir.listFiles()).forEach((t) -> visitClass(t));
		}
	}
}
