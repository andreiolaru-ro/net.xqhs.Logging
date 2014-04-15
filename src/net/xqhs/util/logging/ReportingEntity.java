package net.xqhs.util.logging;

/**
 * Interface for an entity / unit that keeps a log and that needs to report that log to other entities. Updates will
 * be posted at regular intervals of time, and will be incremental (a new post will contain all logging information
 * since the previous successful posting. The messages will contain timestamp, log name, level and content.
 * 
 * @author Andrei Olaru
 */
public interface ReportingEntity
{
	/**
	 * The method will be called at intervals of <code>reportUpdateDelay</code>, if new logging information exist
	 * since the last call.
	 * 
	 * @param content
	 *            - an update from the log containing the new logging information since the last call of this
	 *            function.
	 * @return true if the reporting has completed correctly. Otherwise, the same information will be reported again
	 *         at the next call.
	 */
	public boolean report(String content);
}