package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateUserCommand implements OperationCommand {
    @Autowired
    private UserService userService;


    @Override
    public void execute() {
        userService.addUser(GetConsoleInputValue.getStringValue("Введите логин пользователя"));
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
