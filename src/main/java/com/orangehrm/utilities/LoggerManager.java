package com.orangehrm.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class LoggerManager {
	
	public static Logger getLoggers() {
		return (Logger) LogManager.getLogger();
	}

}
