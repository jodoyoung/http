package kr.co.anajo.config;

import io.netty.util.internal.SystemPropertyUtil;

public interface Environment {

	String docRoot = SystemPropertyUtil.get("user.dir") + "/doc";
}
