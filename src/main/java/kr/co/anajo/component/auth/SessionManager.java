package kr.co.anajo.component.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kr.co.anajo.context.annotation.Component;

@Component
public class SessionManager {
	
	public static String SESSiON_COOKIE_NAME = "AnA_C";

	private Map<String, Session> sessionStore = new ConcurrentHashMap<>();

	public void put(String sessionId, Session session) {
		sessionStore.put(sessionId, session);
	}

	public Session get(String sessionId) {
		return sessionStore.get(sessionId);
	}

}