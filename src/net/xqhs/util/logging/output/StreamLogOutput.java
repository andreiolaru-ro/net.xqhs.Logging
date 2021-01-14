package net.xqhs.util.logging.output;

import java.io.OutputStream;

public interface StreamLogOutput extends LogOutput {
	public void update();
	
	public OutputStream getOutputStream();

}
