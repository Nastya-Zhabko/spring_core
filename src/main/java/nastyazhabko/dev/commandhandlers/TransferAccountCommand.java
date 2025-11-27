package nastyazhabko.dev.commandhandlers;

import nastyazhabko.dev.models.Account;
import nastyazhabko.dev.services.GetConsoleInputValue;
import nastyazhabko.dev.services.AccountService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
public class TransferAccountCommand implements OperationCommand {
    private final AccountService accountService;
    private final GetConsoleInputValue getConsoleInputValue;

    public TransferAccountCommand(AccountService accountService, GetConsoleInputValue getConsoleInputValue) {
        this.accountService = accountService;
        this.getConsoleInputValue = getConsoleInputValue;
    }

    @Override
    public void execute() {
        int senderAccountId = getConsoleInputValue.getIntValue("Введите id аккаунта, с которого будет осуществляться перевод.");
        int recipientAccountId = getConsoleInputValue.getIntValue("Введите id аккаунта, на который будет осуществляться перевод.");
        BigDecimal money = BigDecimal.valueOf(getConsoleInputValue.getIntValue("Введите cумму, которую хотите внести."));
        List<Account> accountList = accountService.transferMoney(senderAccountId, recipientAccountId, money);

        System.out.println("Деньги успешно переведены со счета " + senderAccountId + " на счет "
                + recipientAccountId + ". \nТекущий баланс на счете отправителя " + accountList.get(0).getMoneyAmount()
                + ". \nТекущий баланс на счете получателя " + accountList.get(1).getMoneyAmount());
    }

    @Override
    public ConsoleOperationType getType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
