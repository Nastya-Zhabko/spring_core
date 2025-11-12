package nastyazhabko.dev.commandhandlers;

public interface OperationCommand {
    void execute();
    ConsoleOperationType getType();
}
