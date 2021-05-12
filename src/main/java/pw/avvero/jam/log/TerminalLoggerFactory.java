package pw.avvero.jam.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class TerminalLoggerFactory implements ILoggerFactory {
    @Override
    public Logger getLogger(String name) {
        return new TerminalLogger();
    }
}
