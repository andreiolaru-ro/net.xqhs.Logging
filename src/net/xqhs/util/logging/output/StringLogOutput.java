package net.xqhs.util.logging.output;

/**
 * This interface should be implemented by any {@link LogOutput} which needs the log as a {@link String}. It is updated
 * with a string containing the logging messages (ore one logging message, depending on {@link #getUpdatePeriod()}. If
 * {@link #updateWithEntireLog()} is set, the string will contain all messages so far, or just the most recent messages.
 * 
 * @author Andrei Olaru
 */
public interface StringLogOutput extends LogOutput {
	/**
	 * @param update
	 *            one update containing one or more logging messages.
	 */
	public void update(String update);
	
	/**
	 * @return <code>true</code> if each update should contain the entire log; <code>false</code> if it should contain
	 *         only the messages more recent than the last update.
	 */
	public boolean updateWithEntireLog();
	
}
