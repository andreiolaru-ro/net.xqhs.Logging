package net.xqhs.util.logging.output;

public enum OutputBuilder {
    CONSOLE(ConsoleOutput.class),
    FILE(FileOutput.class)
    ;

    private final Class<? extends LogOutput> outputClass;

    OutputBuilder(Class<? extends LogOutput> consoleOutputClass) {
        this.outputClass = consoleOutputClass;
    }

    static LogOutput buildOutput(OutputBuilder outputBuilder){
        // de instatiat clasa si returnat instanta
        try {
            return outputBuilder.outputClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error creating LogOutput instance", e);
        }
    }
}
