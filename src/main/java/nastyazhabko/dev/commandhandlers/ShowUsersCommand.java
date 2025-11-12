package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowUsersCommand implements OperationCommand {
    @Autowired
    private UserService userService;

    @Override
    public void execute() {
        userService.getAllUsers();
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
