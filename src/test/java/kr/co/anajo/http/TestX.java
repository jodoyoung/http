package kr.co.anajo.http;

import java.nio.file.Paths;

public class TestX {
	
	public static void main(String[] args) {
		String path = "/D:/workspace/anajo/http/target/classes//static";
		path = path.substring(1);
		System.out.println(Paths.get(path));
	}
}
