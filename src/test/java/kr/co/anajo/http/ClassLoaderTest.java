package kr.co.anajo.http;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarFile;

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
		Enumeration<URL> resources = ComponentScanner.class.getClassLoader()
				.getResources("io/netty");
		
		URL rootDirURL = resources.nextElement();
		System.out.println("11111111" + rootDirURL);
		URLConnection con = rootDirURL.openConnection();
		JarFile jarFile;
		String jarFileUrl;
		String rootEntryPath;
		boolean closeJarFile;
 
//		if (con instanceof JarURLConnection) {
//			// Should usually be the case for traditional JAR files.
//			JarURLConnection jarCon = (JarURLConnection) con;
//			ResourceUtils.useCachesIfNecessary(jarCon);
//			jarFile = jarCon.getJarFile();
//			jarFileUrl = jarCon.getJarFileURL().toExternalForm();
//			JarEntry jarEntry = jarCon.getJarEntry();
//			rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
//			closeJarFile = !jarCon.getUseCaches();
//		}
//		else {
//			// No JarURLConnection -> need to resort to URL file parsing.
//			// We'll assume URLs of the format "jar:path!/entry", with the protocol
//			// being arbitrary as long as following the entry format.
//			// We'll also handle paths with and without leading "file:" prefix.
//			String urlFile = rootDirURL.getFile();
//			try {
//				int separatorIndex = urlFile.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
//				if (separatorIndex != -1) {
//					jarFileUrl = urlFile.substring(0, separatorIndex);
//					rootEntryPath = urlFile.substring(separatorIndex + ResourceUtils.JAR_URL_SEPARATOR.length());
//					jarFile = getJarFile(jarFileUrl);
//				}
//				else {
//					jarFile = new JarFile(urlFile);
//					jarFileUrl = urlFile;
//					rootEntryPath = "";
//				}
//				closeJarFile = true;
//			}
//			catch (ZipException ex) {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Skipping invalid jar classpath entry [" + urlFile + "]");
//				}
//				return Collections.emptySet();
//			}
//		}
//
//		try {
//			if (logger.isDebugEnabled()) {
//				logger.debug("Looking for matching resources in jar file [" + jarFileUrl + "]");
//			}
//			if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
//				// Root entry path must end with slash to allow for proper matching.
//				// The Sun JRE does not return a slash here, but BEA JRockit does.
//				rootEntryPath = rootEntryPath + "/";
//			}
//			result = new LinkedHashSet<Resource>(8);
//			for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
//				JarEntry entry = entries.nextElement();
//				String entryPath = entry.getName();
//				if (entryPath.startsWith(rootEntryPath)) {
//					String relativePath = entryPath.substring(rootEntryPath.length());
//					if (getPathMatcher().match(subPattern, relativePath)) {
//						result.add(rootDirResource.createRelative(relativePath));
//					}
//				}
//			}
//			return result;
//		}
//		finally {
//			if (closeJarFile) {
//				jarFile.close();
//			}
//		}
	}

}