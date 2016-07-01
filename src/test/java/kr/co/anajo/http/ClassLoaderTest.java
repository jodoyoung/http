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
		Enumeration<URL> resources = getClass().getClassLoader().getResources("kr/co/anajo");
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
				System.out.println("##############class file : " + dir);
			}
		} else {
			Arrays.stream(dir.listFiles()).forEach((t) -> visitClass(t));
		}
	}
	
}