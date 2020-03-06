package net.xqhs.util.logging.wrappers;

import net.xqhs.util.logging.LoggerSimple;
import net.xqhs.util.logging.logging.LogWrapper;

import java.io.IOException;
import java.io.OutputStream;

public class GlobalLogWrapper extends LogWrapper {
    /**
     * The output stream which gets the output of the log
     */
    private static OutputStream logStream = null;
    /**
     * The current level of the log wrapped
     */
    private static LoggerSimple.Level currentLevel = LoggerSimple.Level.INFO;
    /**
     * The name fo the log.
     */
    String name = null;

    /**
     * Creates a new console wrapper log, with the specified name.
     *
     * @param logName - the name of the log to be created.
     */
    public GlobalLogWrapper(String logName)
    {
        name = logName;
    }

    /**
     * Set a custom OutputStream to which the output will get
     * written to. This applies to all log messages
     * @param stream a custom OutputStream
     */
    public static void setLogStream(OutputStream stream) {
        logStream = stream;
    }

    /**
     * Set level of the log. This is a global level and
     * will be applied to all Wrapper of Global type
     * @param level the new level
     */
    @Override
    public void setLevel(LoggerSimple.Level level) {
        currentLevel = level;
    }

    /**
     * Unused in this implementation of the LogWrapper
     * @param format
     *            - a pattern, in a format that is potentially characteristic to the wrapper.
     * @param destination
     */
    @Override
    protected void addDestination(String format, OutputStream destination) {
        // Unused for this implementation
    }

    @Override
    public void l(LoggerSimple.Level level, String message) {
        if (level.displayWith(currentLevel)) {
            if (logStream != null) {
                try {
                    logStream.write((message + "\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void exit() {
        setLevel(LoggerSimple.Level.OFF);
        logStream = null;
    }
}
