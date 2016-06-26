package kr.co.anajo;

import java.util.logging.Logger;

import kr.co.anajo.context.DatabaseContext;
import kr.co.anajo.context.LoggingContext;

public class Main {

	private static final Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		LoggingContext.initialize();
		logger.info("Server Start...");
		long startTime = System.currentTimeMillis();
		
		DatabaseContext.initialize();
		// TODO component scan
		// TODO anno-profile
		// TODO anno-config
		// TODO anno-cnotroller
		// TODO netty http bind
		// TODO redis cache
		// TODO Mbean
		// TODO index
		
		logger.info(() -> String.format("Server Startup. time: %d", (System.currentTimeMillis() - startTime)));
	}
}