package kr.co.anajo.http.handler;

import java.util.ArrayList;
import java.util.List;

import kr.co.anajo.context.annotation.Component;

@Component
public class PathMatcher {

	private List<String> ignoreAuthenticationUris = new ArrayList<String>() {
		{
			add("/auth");
		}
	};

	public boolean isStaticUri(String uri) {
		if (uri != null && ("favicon.ico".equalsIgnoreCase(uri) || uri.startsWith("/static"))) {
			return true;
		}
		return false;
	}

	public boolean isAuthenticationIgnoreUri(String uri) {
		if (isContains(ignoreAuthenticationUris, uri)) {
			return true;
		}
		return false;
	}

	public boolean isContains(List<String> uris, String uri) {
		if (uris == null) {
			return false;
		}
		for (String u : uris) {
			if (uri.startsWith(u)) {
				return true;
			}
		}
		return false;
	}
}