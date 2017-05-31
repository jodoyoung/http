package kr.co.anajo.http;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.annotation.Resource;

import org.junit.Test;

import kr.co.anajo.context.ComponentScanner;

public class ClassLoaderTest {

	private String regex = "^(.+)(\\.class)$";

	// @Test
	public void loadResources() throws Exception {
		String basePackage = "kr.co.anajo";

		Enumeration<URL> resources = getClass().getClassLoader().getResources(basePackage.replace(".", "/"));
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			File dir = new File(url.toURI());
			visitClass(dir);
		}
	}

	private void visitClass(File dir) {
		if (dir.isFile()) {
			if (dir.toString().matches(regex)) {
				try {
					String classFilePullPath = dir.getAbsolutePath().replace(File.separator, ".");
					String className = classFilePullPath.substring(classFilePullPath.indexOf("kr.co.anajo"),
							classFilePullPath.lastIndexOf(".class"));
					Class klass = getClass().getClassLoader().loadClass("kr.co.anajo.Main");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else {
			Arrays.stream(dir.listFiles()).forEach((t) -> visitClass(t));
		}
	}

	@Test
	public void jar() throws Exception {
		Enumeration<URL> resources = ComponentScanner.class.getClassLoader().getResources("io/netty");

		URL rootDirURL = resources.nextElement();
		System.out.println("rootDirURL - " + rootDirURL);

		// if (con instanceof JarURLConnection) {
		// // Should usually be the case for traditional JAR files.
		URLConnection con = rootDirURL.openConnection();
		JarURLConnection jarCon = (JarURLConnection) con;
		// ResourceUtils.useCachesIfNecessary(jarCon);
		JarFile jarFile = jarCon.getJarFile();
		System.out.println("jarFile - " + jarFile);
		
		String jarFileUrl = jarCon.getJarFileURL().toExternalForm();
		System.out.println("jarFileUrl - " + jarFileUrl);
		
		JarEntry jarEntry = jarCon.getJarEntry();
		System.out.println("jarEntry - " + jarEntry);
		
		String rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
		System.out.println("rootEntryPath - " + rootEntryPath);
		
		boolean closeJarFile = !jarCon.getUseCaches();
		System.out.println("closeJarFile - " + closeJarFile);

		if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
			// Root entry path must end with slash to allow for proper
			// matching.
			// The Sun JRE does not return a slash here, but BEA JRockit
			// does.
			rootEntryPath = rootEntryPath + "/";
			System.out.println("rootEntryPath - " + rootEntryPath);
		}
		for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			System.out.println("entry - " + entry);
			
//			String entryPath = entry.getName();
//			System.out.println("entryPath - " + entryPath);
//			
//			if (entryPath.startsWith(rootEntryPath)) {
//				String relativePath = entryPath.substring(rootEntryPath.length());
//				System.out.println("relativePath - " + relativePath);
////				if (getPathMatcher().match(subPattern, relativePath)) {
////					
////					result.add(rootDirResource.createRelative(relativePath));
////				}
//			}
		}
		
		if (closeJarFile) {
			jarFile.close();
		}
}

}