


  * level writing
  * attach timer to the global Logging instance	
  * tabbed space for names that adapts to the last n entries
  * remove wrapper type enum
  * implement and test link data use
  * M-log should monitor all logs (but they are not registered), log number of active logs
    * implement setting MasterLog.exitWithLastLog()
    
  * what if some logs have performance mode and other don't - log messages will be scrambled.
  * highlighting mode is instantaneous, and is incompatible with delayed showing of logs, done above the log wrapper
  * padding whould be split into highlighted and not-highlighted logs
  * masterLog exit means all logs exit? Should this be correct?
  
  * Documentation (including updateing already documented items