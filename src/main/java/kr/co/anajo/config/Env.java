package kr.co.anajo.config;

import io.netty.util.internal.SystemPropertyUtil;

public interface Env {

	String docRoot = SystemPropertyUtil.get("user.dir") + "/doc";
}
	