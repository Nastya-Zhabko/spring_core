package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WithdrawAccountCommand implements OperationCommand{
    @Autowired
    private AccountService accountService;

    @Override
    public void execute() {
        accountService.subtractMoney(GetConsoleInputValue.getIntValue("Введите id аккаунта, с которого требуется снять деньги."),
                            (long) GetConsoleInputValue.getIntValue("Введите cумму, которую хотите снять."));
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}
