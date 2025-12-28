package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.AccountService;
import org.springframework.stereotype.Component;

@Component
public class CloseAccountCommand implements OperationCommand {
    private final AccountService accountService;
    private final GetConsoleInputValue getConsoleInputValue;

    public CloseAccountCommand(AccountService accountService, GetConsoleInputValue getConsoleInputValue) {
        this.accountService = accountService;
        this.getConsoleInputValue = getConsoleInputValue;
    }

    @Override
    public void execute() {
        System.out.println("Введите id аккаунта, который требуется закрыть.");
        accountService.closeAccount(getConsoleInputValue.getIntValue());
        System.out.println("Аккаунт успешно закрыт!");
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
