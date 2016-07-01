package kr.co.anajo;

import java.util.logging.Logger;

import kr.co.anajo.context.ComponentScanner;
import kr.co.anajo.context.LoggingContext;

public class Main {

	private static final Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		LoggingContext.initialize();
		logger.info("Server Start...");
		long startTime = System.currentTimeMillis();

		try {
			ComponentScanner.scan("kr/co/anajo");
		} catch (Throwable t) {
			logger.severe(() -> String.format("component scan failed! %d", t));
		}
		// TODO component scan
		// TODO di
		// TODO anno-profile
		// TODO anno-config

		// TODO annotation config
		// DatabaseContext.initialize();

		// TODO anno-cnotroller
		// TODO netty http bind
		// TODO redis cache
		// TODO Mbean
		// TODO index

		logger.info(() -> String.format("Server Startup. time: %d", (System.currentTimeMillis() - startTime)));
	}
}