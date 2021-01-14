package testing;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.xqhs.util.logging.Logger.Level;
import net.xqhs.util.logging.Unit;
import net.xqhs.util.logging.UnitComponent;

public class LogTester2 extends LogTester {
	
	class UnitTester extends Unit {
		
	}
	
	public static void main(String[] args) {
		
		System.out.println("\n\n=================== Unit Component ====================\n\n");
		
		Map<String, UnitComponent> logs = new HashMap<>();
		
		String[] sources = new String[] { "Short", "Short2", "VSh", "MediumSource", "A-Longer-Source",
				"A-Very-Long-Logging-Source" };
		for(String source : sources)
			logs.put(source, new UnitComponent(source));
		
		Random rand = new Random();
		for(int i = 0; i < 50; i++) {
			String source = sources[rand.nextInt(sources.length)];
			Level level = levelPicker();
			logs.get(source).l(level, "Some [] logging message", level.toString());
		}
		
		System.out.println("\n\n=================== Backwards compatibility ====================\n\n");
		
		// test console logger
		
	}
}
