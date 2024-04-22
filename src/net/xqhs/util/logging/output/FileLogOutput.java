package net.xqhs.util.logging.output;

import java.io.FileOutputStream;

public interface FileLogOutput extends LogOutput{

    public void update();

    public FileOutputStream getOutputStream();

    public void exit();
}
