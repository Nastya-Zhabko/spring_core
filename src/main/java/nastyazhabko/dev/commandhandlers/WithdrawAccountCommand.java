package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.models.Account;
import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithdrawAccountCommand implements OperationCommand {
    private final AccountService accountService;
    private final GetConsoleInputValue getConsoleInputValue;

    public WithdrawAccountCommand(AccountService accountService, GetConsoleInputValue getConsoleInputValue) {
        this.accountService = accountService;
        this.getConsoleInputValue = getConsoleInputValue;
    }

    @Override
    public void execute() {
        int accountId = getConsoleInputValue.getIntValue("Введите id аккаунта, с которого требуется снять деньги.");
        BigDecimal money = BigDecimal.valueOf(getConsoleInputValue.getIntValue("Введите cумму, которую хотите снять."));

        Account account = accountService.subtractMoney(accountId, money);

        System.out.println("Деньги успешно сняты со счета " + accountId + " Текущий баланс: " + account.getMoneyAmount());
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}
