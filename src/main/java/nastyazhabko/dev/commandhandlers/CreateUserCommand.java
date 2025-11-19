package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.models.User;
import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class CreateUserCommand implements OperationCommand {
    private final UserService userService;
    private final GetConsoleInputValue getConsoleInputValue;

    public CreateUserCommand(UserService userService, GetConsoleInputValue getConsoleInputValue) {
        this.userService = userService;
        this.getConsoleInputValue = getConsoleInputValue;
    }

    @Override
    public void execute() {
        User user = userService.addUser(getConsoleInputValue.getStringValue("Введите логин пользователя"));
        System.out.println("Пользователь успешно создан: " + user);
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
