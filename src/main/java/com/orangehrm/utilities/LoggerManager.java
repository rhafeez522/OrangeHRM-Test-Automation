package com.orangehrm.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {

    //this method return logger instance for the provided class

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger();

    }
}
