package kr.co.anajo.config;

import java.nio.file.Paths;

public interface Env {

	String docRoot = Paths.get(Env.class.getResourceAsStream("/").toString()).resolve("static").toString();

}