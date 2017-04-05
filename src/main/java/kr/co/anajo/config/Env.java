package kr.co.anajo.config;

import java.nio.file.Paths;

import io.netty.util.internal.SystemPropertyUtil;

public interface Env {

	String docRoot = Paths.get(SystemPropertyUtil.get("user.dir")).resolve("src").resolve("main").resolve("static").toString();
}
	