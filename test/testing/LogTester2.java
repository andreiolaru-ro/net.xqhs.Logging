package testing;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.xqhs.util.logging.Debug.DebugItem;
import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.MasterLog;
import net.xqhs.util.logging.Unit;
import net.xqhs.util.logging.UnitComponent;
import net.xqhs.util.logging.output.ConsoleOutput;
import net.xqhs.util.logging.output.FileOutput;

@SuppressWarnings("javadoc")
public class LogTester2 {
	
	static class UnitTester extends Unit {
		public UnitTester(String name) {
			super();
			setUnitName(name);
		}
		
		@Override
		public Unit setLogLevel(Level logLevel) {
			return super.setLogLevel(logLevel);
		}
		
		@Override
		public void doExit() {
			super.doExit();
		}
	}
	
	enum LocalDebug implements DebugItem {
		DO_DEBUG(true)
		
		;
		
		boolean value;
		
		private LocalDebug(boolean value) {
			this.value = value;
		}
		
		@Override
		public boolean toBool() {
			return value;
		}
	}
	
	public static String someMethod(UnitComponent log) {
		return (String) log.lr("test", "i am [] here", "standing");
	}
	
	public static Level levelPicker() {
		int r = new Random().nextInt(10);
		if(r < 1)
			return Level.ERROR;
		if(r < 3)
			return Level.WARN;
		if(r < 7)
			return Level.INFO;
		return Level.TRACE;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("\n\n=================== Unit Component ====================\n\n");
//		MasterLog.enablePerformanceModeTools(2000);
//		MasterLog.activateGlobalPerformanceMode();
		MasterLog.addDefaultOutput(new ConsoleOutput());
		MasterLog.addDefaultOutput(new FileOutput("test/testing/file/testOutput.txt"));




		Map<String, UnitComponent> logs = new HashMap<>();
		// MasterLog.setDefaultLogLevel(Level.OFF);
		
		String[] sources = new String[] { "Short", "Short2", "VSh", "MediumSource", "A-Longer-Source",
				"A-Very-Long-Logging-Source" };
		int idx = 0;
		UnitTester unit[] = new UnitTester[] { new UnitTester("1"), new UnitTester("2"), new UnitTester("3") };
		unit[0].setLogLevel(Level.ALL);
		for(String source : sources) {
			int ip = idx / 2;
			idx++;
			logs.put(source, new UnitComponent(source).addParentUnit(unit[ip]).setLogLevel(Level.INFO));
		}
		unit[2].setLogLevel(Level.ERROR);
		
		logs.get("Short").setHighlighted();
		
		Random rand = new Random();
		for(int i = 0; i < 100; i++) {
			String source = sources[rand.nextInt(sources.length)];
			Level level = levelPicker();
			logs.get(source).l(level, "Some [] logging message", level.toString());
			try {
				Thread.sleep(50);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			if(i == 70)
				logs.get("Short").setNotHighlighted();
			// if(i == 50)
			// logs.get("VSh").setNotHighlighted();
			if(i == 30)
				logs.get("VSh").setPerformanceMode(true);
		}
		
		logs.get("VSh").le("Last message");
		for(String source : sources)
			logs.get(source).doExit();
		for(UnitTester u : unit)
			u.doExit();
		// MasterLog.doExit();
		System.out.println("\n\n=================== Backwards compatibility ====================\n\n");
		
		// test console logger
		System.out.println("\n\n=================== END ====================\n\n");
	}
}
