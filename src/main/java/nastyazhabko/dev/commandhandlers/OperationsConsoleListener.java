package nastyazhabko.dev.commandhandlers;

import java.util.*;

import nastyazhabko.dev.services.GetConsoleInputValue;
import org.springframework.stereotype.Component;

@Component
public class OperationsConsoleListener implements Runnable {

    private Map<ConsoleOperationType, OperationCommand> commandMap;
    private volatile boolean running = true;
    private final GetConsoleInputValue getConsoleInputValue;

    public OperationsConsoleListener(List<OperationCommand> commands, GetConsoleInputValue getConsoleInputValue) {
        this.getConsoleInputValue = getConsoleInputValue;
        commandMap = new HashMap<>();
        commands.forEach(command -> commandMap.put(command.getType(), command));
    }

    @Override
    public void run() {
        while (running) {
            try {
                String command = getConsoleInputValue.getStringValue("Введите команду, которую вы хотите выполнить: ");

                ConsoleOperationType operationType = parseCommand(command);

                if (operationType == ConsoleOperationType.EXIT) {
                    running = false;
                    break;
                }
                try {
                    commandMap.get(operationType).execute();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Введена команда, которой нет в списке команд " + e.getMessage() + ". Введите существующую команду");
            }
        }
        System.out.println("Программа завершена.");
    }

    private ConsoleOperationType parseCommand(String command) {
        try {
            return ConsoleOperationType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(command);
        }
    }
}