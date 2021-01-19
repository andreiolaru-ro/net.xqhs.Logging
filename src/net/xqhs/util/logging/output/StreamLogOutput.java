package net.xqhs.util.logging.output;

import java.io.OutputStream;

/**
 * This interface should be implemented by any {@link LogOutput} which outputs logging messages directly to a stream
 * (e.g. <code>System.out</code>).
 * <p>
 * {@link #getOutputStream()} should always return the stream.
 * <p>
 * It is the job of the instance to manage the stream (e.g. close it if necessary).
 * 
 * @author Andrei Olaru
 */
public interface StreamLogOutput extends LogOutput {
	/**
	 * This method is called whenever there are new logging messages (or more rearly, according to
	 * {@link #getUpdatePeriod()}.
	 */
	public void update();
	
	/**
	 * @return the stream to write logging messages to.
	 */
	public OutputStream getOutputStream();
	
	/**
	 * The method is called when the output is removed from the list of outputs.
	 */
	public void exit();
}
