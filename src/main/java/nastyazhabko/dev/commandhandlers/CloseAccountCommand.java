package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CloseAccountCommand implements OperationCommand {
    @Autowired
    private AccountService accountService;

    @Override
    public void execute() {
        accountService.closeAccount(GetConsoleInputValue.getIntValue("Введите id аккаунта, который требуется закрыть."));
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
