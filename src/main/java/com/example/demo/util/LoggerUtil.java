package com.example.demo.util;

import org.slf4j.Logger;

public class LoggerUtil {


	public static void logInfo(String message, Logger log) {
		if (log.isInfoEnabled())
			log.info(message);
	}

	public static void logDebug(String message, Logger log) {
		if (log.isDebugEnabled())
			log.debug(message);
	}

	public static void logError(String message, Throwable t, Logger log) {
		if (log.isErrorEnabled())
			log.error(message, t);
	}

}
