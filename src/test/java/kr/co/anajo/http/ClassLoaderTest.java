package kr.co.anajo.http;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

import org.junit.Test;

public class ClassLoaderTest {

	@Test
	public void loadResources() throws Exception {
		Enumeration<URL> resources = getClass().getClassLoader().getResources("kr/co/anajo");
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			Path path = Paths.get(url.toURI());
			System.out.println(path);
		}
	}
}
