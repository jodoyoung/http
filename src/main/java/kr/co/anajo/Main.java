package kr.co.anajo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.anajo.context.ApplicationContext;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		logger.info("Server Startup...");

		try {
			ApplicationContext context = new ApplicationContext("kr.co.anajo");
			context.start();
		} catch (Throwable t) {
			logger.error("application start failed!", t);
			System.exit(1);
		}
<<<<<<< HEAD
=======

		// TODO redis cache
		// TODO Mbean
		// TODO index
		logger.info("Server Startup. time: {}", System.currentTimeMillis() - startTime);
>>>>>>> branch 'master' of https://github.com/jodoyoung/http.git
	}
}