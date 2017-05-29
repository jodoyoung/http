package kr.co.anajo.config;

public interface Env {

	String docRoot = Env.class.getResource("/").getPath();
	
	public static void main(String[] args) {
		System.out.println(docRoot);
	}

}