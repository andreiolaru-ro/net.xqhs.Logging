package net.xqhs.util.logging.output;

public class OutputManager {
    private OutputBuilder outputBuilder;

    public OutputManager(OutputBuilder outputBuilder) {
        this.outputBuilder = outputBuilder;
    }
    public void output(String message) {
        switch (outputBuilder) {
            case CONSOLE:
                // Logic to write to a console using ConsoleOutput
                break;
            case FILE:
                // Logic to write to a file
                break;
            default:
                throw new IllegalArgumentException("Invalid output option");
        }
    }
}
