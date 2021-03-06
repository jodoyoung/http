package kr.co.anajo.http.handler;

import java.util.ArrayList;
import java.util.List;

import kr.co.anajo.context.annotation.Component;

@Component
public class URLMatcher {

	private List<String> ignoreAuthenticationUris = new ArrayList<String>() {
		private static final long serialVersionUID = -1419472799315132362L;

		{
			add("/auth");
			add("/favicon.ico");
			add("/static");
		}
	};

	public boolean isStaticUri(String uri) {
		if (uri != null && ("/favicon.ico".equalsIgnoreCase(uri) || uri.startsWith("/static"))) {
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