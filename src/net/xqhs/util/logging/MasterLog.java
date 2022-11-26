package net.xqhs.util.logging;

import java.util.concurrent.LinkedBlockingQueue;

import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.Unit.LogEntry;

/**
 * The class manages static, global settings that span over all logs. Among these are:
 * <ul>
 * <li>setting the default {@link java.util.logging.Level} for new logs
 * <li>setting global <i>performance mode</i> and management of performance mode elements, namely the thread and queue
 * used for processing logging messages.
 * </ul>
 * <p>
 * It contains a singleton {@link Unit} (the "master" unit) which is added automatically as parent to all logs.
 * 
 * @author Andrei Olaru
 */
public class MasterLog {
	/**
	 * The default level for newly-created logs.
	 */
	protected static Level							defaultLevel			= Level.ALL;
	/**
	 * The unit which is given as parent to all logs.
	 */
	protected static Unit							masterLog				= new Unit().setUnitName("M-Log");
	/**
	 * The global switch for performance mode. This can be switched to <code>true</code> <b>only</b> before any logging
	 * begins.
	 * <p>
	 * Afterwards, individual logs can be set to performance mode, before they begin any logging.
	 * <p>
	 * The global performance mode cannot be disabled.
	 */
	protected static boolean						GLOBAL_PERFORMANCE_MODE	= false;
	/**
	 * For <i>performance mode</i>, this stores the messages that need to be posted to the log.
	 * <p>
	 * The availability of the performance mode itself is indicated by the fact that the queue is not <code>null</code>.
	 */
	protected static LinkedBlockingQueue<LogEntry>	processingQueue			= null;
	/**
	 * For logs in <i>performance mode</i>, this is the thread that processes the logging messages.
	 */
	protected static Thread							processingThread		= null;
	/**
	 * For logs in <i>performance mode</i>, this is the (minimum) time between writings to the output, in milliseconds.
	 */
	protected static long							TIME_STEP				= 1000;
	
	/**
	 * The level of the master unit, which may propagate to other logs.
	 * 
	 * @param level
	 *            - the {@link Level}.
	 */
	public static void setLogLevel(Level level) {
		masterLog.setLogLevel(level);
	}
	
	/**
	 * @return the current default level for newly-created logs.
	 */
	public static Level getDefaultLevel() {
		return defaultLevel;
	}
	
	/**
	 * Changes the default level for newly-created logs. There is <b>no effect</b> on already existing logs.
	 * 
	 * @param level
	 *            - the new default level.
	 * @return the previous default level.
	 */
	public static Level setDefaultLogLevel(Level level) {
		Level previous = defaultLevel;
		defaultLevel = level;
		return previous;
	}
	
	/**
	 * @return the global performance mode.
	 */
	public static boolean isGlobalPerformanceModeSet() {
		return GLOBAL_PERFORMANCE_MODE;
	}
	
	/**
	 * Activates the global performance mode. This can be switched to <code>true</code> <b>only</b> before any logging
	 * begins.
	 * <p>
	 * Afterwards, individual logs can be set to performance mode, before they begin any logging.
	 * <p>
	 * The global performance mode cannot be disabled.
	 */
	public static void activateGlobalPerformanceMode() {
		if(masterLog.lockedR())
			return;
		GLOBAL_PERFORMANCE_MODE = true;
		enablePerformanceModeTools();
		masterLog.lf("Global performance mode active");
	}
	
	/**
	 * This has to be called (and is called by {@link Unit}) whenever there exists a log that needs performance mode.
	 */
	public static void enablePerformanceModeTools() {
		if(processingQueue != null)
			// already enabled
			return;
		processingQueue = new LinkedBlockingQueue<>();
		processingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				processQueue();
			}
		});
		processingThread.start();
	}
	
	/**
	 * Same as {@link #enablePerformanceModeTools()}, but allows setting the period at which log messages are processed.
	 * <p>
	 * This is a minimum, it may be that more time passes between processing chunks of messages.
	 * 
	 * @param loggingPeriod
	 *            - the minimal time from one stage of processing to the next.
	 */
	public static void enablePerformanceModeTools(long loggingPeriod) {
		TIME_STEP = loggingPeriod;
		enablePerformanceModeTools();
	}
	
	/**
	 * Processes the logging entries so far.
	 */
	protected static void processQueue() {
		while(processingQueue != null) {
			// lf("Messages: ", Integer.valueOf(messageQueue.size()));
			try {
				Thread.sleep(TIME_STEP);
			} catch(InterruptedException e1) {
				// don't do anything
			}
			processAll();
		}
	}
	
	protected static void processAll() {
		while(processingQueue != null && !processingQueue.isEmpty()) {
			LogEntry entry = processingQueue.poll();
			entry.log.l(entry.level, Unit.compose(entry.message, entry.objects));
		}
	}

	public static void doExit() {
		masterLog.doExit();
	}

	protected static void closePerformaceElements() {
		if(processingQueue != null)
			processAll();
		processingQueue = null;
		if(processingThread != null)
			try {
				processingThread.interrupt();
				processingThread.join();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		processingThread = null;		
	}
}
