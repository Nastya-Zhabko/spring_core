package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.models.Account;
import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositAccountCommand implements OperationCommand {
    private final AccountService accountService;
    private final GetConsoleInputValue getConsoleInputValue;

    public DepositAccountCommand(AccountService accountService, GetConsoleInputValue getConsoleInputValue) {
        this.accountService = accountService;
        this.getConsoleInputValue = getConsoleInputValue;
    }

    @Override
    public void execute() {
        System.out.println("Введите id аккаунта, который требуется пополнить.");
        int accountId = getConsoleInputValue.getIntValue();
        System.out.println("Введите cумму, которую хотите внести.");
        BigDecimal money = BigDecimal.valueOf(getConsoleInputValue.getIntValue());
        Account account = accountService.addMoney(accountId, money);
        System.out.println("Счет " + accountId + " успешно пополнен на " + money + ". Текущий баланс: " + account.getMoneyAmount());
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
