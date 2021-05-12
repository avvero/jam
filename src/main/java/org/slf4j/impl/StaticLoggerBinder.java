package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import pw.avvero.jam.log.TerminalLoggerFactory;

public class StaticLoggerBinder {

    private static final StaticLoggerBinder instance = new StaticLoggerBinder();

    public static StaticLoggerBinder getSingleton() {
        return instance;
    }

    public ILoggerFactory getLoggerFactory() {
        return new TerminalLoggerFactory();
    }

}
