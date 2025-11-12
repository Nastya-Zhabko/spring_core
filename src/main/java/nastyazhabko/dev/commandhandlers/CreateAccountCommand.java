package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountCommand implements OperationCommand {
    @Autowired
    private AccountService accountService;

    @Override
    public void execute() {
        accountService.createAccount(GetConsoleInputValue.getIntValue("Введите id пользователя, для которого нужно создать аккаунт."));
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
