package kr.co.anajo.http.handler;

import java.util.List;

import kr.co.anajo.context.annotation.Component;

@Component
public class PathMatcher {

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