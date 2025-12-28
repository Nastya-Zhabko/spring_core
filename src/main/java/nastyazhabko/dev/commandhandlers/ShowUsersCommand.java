package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class ShowUsersCommand implements OperationCommand {
    private final UserService userService;

    public ShowUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute() {
        userService.getAllUsers().stream().forEach(System.out::println);
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
