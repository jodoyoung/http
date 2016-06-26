package kr.co.anajo.context;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingContext {

	public static void initialize() {
		String format = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-1s [%2$s] %5$s %6$s%n";
		System.setProperty("java.util.logging.SimpleFormatter.format", format);

		SimpleFormatter simpleFormatter = new SimpleFormatter();

		final ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.FINEST);
		consoleHandler.setFormatter(simpleFormatter);

		final Logger applicationLogger = Logger.getLogger("kr.co.anajo.http");
		applicationLogger.setLevel(Level.INFO);
		applicationLogger.addHandler(consoleHandler);

		try {
			final FileHandler fileHandler = new FileHandler("http", Integer.MAX_VALUE, 10);
			fileHandler.setFormatter(simpleFormatter);
			applicationLogger.addHandler(fileHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}