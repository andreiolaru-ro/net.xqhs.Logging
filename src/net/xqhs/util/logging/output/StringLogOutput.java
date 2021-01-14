package net.xqhs.util.logging.output;

import java.io.OutputStream;

public interface StringLogOutput extends LogOutput {
	public void update(String update);
	
	public boolean updateWithEntireLog();
	
}
