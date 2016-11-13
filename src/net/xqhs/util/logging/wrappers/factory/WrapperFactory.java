package net.xqhs.util.logging.wrappers.factory;

import net.xqhs.util.logging.logging.LogWrapper;
import net.xqhs.util.logging.wrappers.ConsoleWrapper;
import net.xqhs.util.logging.wrappers.JavaLogWrapper;
import net.xqhs.util.logging.wrappers.Log4JWrapper;

public class WrapperFactory {
    static WrapperFactory singleton = null;
    
    private WrapperFactory(){}
    
    public static WrapperFactory getInst(){
        if(singleton == null)
            singleton = new WrapperFactory();
        return singleton;
    }
    
    public LogWrapper newInst(LogWrapper.LoggerType loggerType, String loggerName){
        switch(loggerType){
        case CONSOLE:
            return new ConsoleWrapper(loggerName);
        case LOG4J:
            return new Log4JWrapper(loggerName);
        case JAVA:
            return new JavaLogWrapper(loggerName);
        default:
            return null;
        }
    }
}
