package project;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;

public class MyLogger {

	private static final String PATH = "./logFile.log";

	private static MyLogger instance = null;
	private Logger logger = Logger.getLogger("myLogger");
	private FileHandler fh;

	private MyLogger() throws SecurityException, IOException {
		this.fh = new FileHandler(PATH);
		this.logger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
	}

	public static MyLogger getSingletonInstance() throws SecurityException, IOException {
		if (instance == null)
			instance = new MyLogger();
		return instance;
	}

	public void saveMess(String message) {
		this.logger.info(message);
	}
}
