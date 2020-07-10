package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

	private static final Logger log = LoggerFactory.getLogger(LoggerUtil.class);

	public static void logInfo(String message) {
		if (log.isInfoEnabled())
			log.info(message);
	}

	public static void logDebug(String message) {
		if (log.isDebugEnabled())
			log.debug(message);
	}

	public static void logError(String message, Throwable t) {
		if (log.isErrorEnabled())
			log.error(message, t);
	}

}
