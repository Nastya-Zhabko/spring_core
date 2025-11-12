package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferAccountCommand implements OperationCommand {
    @Autowired
    private AccountService accountService;

    @Override
    public void execute() {
        accountService.transferMoney(GetConsoleInputValue.getIntValue("Введите id аккаунта, с которого будет осуществляться перевод."),
                GetConsoleInputValue.getIntValue("Введите id аккаунта, на который будет осуществляться перевод."),
                (long) GetConsoleInputValue.getIntValue("Введите cумму, которую хотите внести."));
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
