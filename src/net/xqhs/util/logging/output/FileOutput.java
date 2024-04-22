package net.xqhs.util.logging.output;

import net.xqhs.util.logging.Logger;

import java.io.*;

public class FileOutput implements StreamLogOutput{
    private final FileOutputStream outputStream;

    public FileOutput(String path) throws FileNotFoundException {
        outputStream = new FileOutputStream(path);
    }

    @Override
    public void update() {

    }

    @Override
    public FileOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void exit() {

    }

    @Override
    public int getUpdatePeriod() {
        return 0;
    }

    @Override
    public int formatData() {
        return Logger.INCLUDE_NAME;
    }

    @Override
    public boolean useCustomFormat() {
        return false;
    }

    @Override
    public String format(Logger.Level level, String source, String message) {
        return null;
    }
}
