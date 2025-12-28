package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.models.Account;
import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.AccountService;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountCommand implements OperationCommand {
    private final AccountService accountService;
    private final GetConsoleInputValue getConsoleInputValue;

    public CreateAccountCommand(AccountService accountService, GetConsoleInputValue getConsoleInputValue) {
        this.accountService = accountService;
        this.getConsoleInputValue = getConsoleInputValue;
    }

    @Override
    public void execute() {
        System.out.println("Введите id пользователя, для которого нужно создать аккаунт.");
        Account account = accountService.createAccount(getConsoleInputValue.getIntValue());
        System.out.println("Счет успешно создан: " + account);
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
