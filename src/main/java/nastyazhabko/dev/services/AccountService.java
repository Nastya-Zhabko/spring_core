package nastyazhabko.dev.services;

import nastyazhabko.dev.exceptions.DeleteLastUserAccountException;
import nastyazhabko.dev.properties.AccountProperties;
import nastyazhabko.dev.models.Account;
import nastyazhabko.dev.models.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class AccountService {
    private Map<Integer, Integer> usersAccounts = new HashMap<>();
    private Map<Integer, Account> accounts = new HashMap<>();
    private int idGenerator = 0;

    private final UserService userService;
    private final AccountProperties accountProperties;

    public AccountService(@Lazy UserService userService, AccountProperties accountProperties) {
        this.userService = userService;
        this.accountProperties = accountProperties;
    }

    public Account createAccount(int userId) {
        User user;
        Account account;

        try {
            user = userService.getUserById(userId);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        }

        if (user.getAccountList().isEmpty()) {
            account = new Account(idGenerator, userId, accountProperties.getDefaultAmount());
        } else {
            account = new Account(idGenerator, userId, BigDecimal.ZERO);
        }
        accounts.put(account.getId(), account);
        user.addAccount(account);
        usersAccounts.put(idGenerator, userId);
        idGenerator++;
        return account;

    }

    public Account addMoney(int accountId, BigDecimal money) {
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NullPointerException("Ошибка: Укажите положительную не нулевую сумму!");
        }
        if (!usersAccounts.containsKey(accountId)) {
            throw new NoSuchElementException("Ошибка: Счета с id " + accountId + " не существует.");
        } else {
            Account account = accounts.get(accountId);
            account.addMoney(money);
            return account;
        }
    }

    public Account subtractMoney(int accountId, BigDecimal money) {
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NullPointerException("Ошибка: Укажите положительную не нулевую сумму!");
        }
        if (!usersAccounts.containsKey(accountId)) {
            throw new NoSuchElementException("Ошибка: Счета с id " + accountId + " не существует.");
        } else {
            try {
                Account account = accounts.get(accountId);
                account.subtractMoney(money);
                return account;
            } catch (IllegalStateException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    public List<Account> transferMoney(int senderAccountId, int recipientAccountId, BigDecimal money) {
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Ошибка: укажите положительную не нулевую сумму!");
        }
        if (!usersAccounts.containsKey(senderAccountId) && !usersAccounts.containsKey(recipientAccountId)) {
            throw new NoSuchElementException("Ошибка: Счета отправителя с id " + senderAccountId + " и счета получателя с id " + recipientAccountId + " не существует.");
        }
        if (!usersAccounts.containsKey(senderAccountId)) {
            throw new NoSuchElementException("Ошибка: Счета отправителя с id " + senderAccountId + " не существует.");
        }
        if (!usersAccounts.containsKey(recipientAccountId)) {
            throw new NoSuchElementException("Ошибка: Счета получателя с id " + recipientAccountId + " не существует.");
        }
        if (senderAccountId == recipientAccountId) {
            throw new IllegalArgumentException("Ошибка: В качестве отправителя и получателя указан один счет.");
        }
        Account senderAccount = accounts.get(senderAccountId);
        Account recipientAccount = accounts.get(recipientAccountId);

        if (usersAccounts.get(senderAccountId) == usersAccounts.get(recipientAccountId)) {
            try {
                senderAccount.subtractMoney(money);
            } catch (IllegalStateException e) {
                throw new IllegalArgumentException(e.getMessage());
            }

        } else {
            try {
                senderAccount.subtractMoney((money.add((money.multiply(accountProperties.getDefaultCommission())).divide(BigDecimal.valueOf(100)))));
            } catch (IllegalStateException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        recipientAccount.addMoney(money);
        return List.of(senderAccount, recipientAccount);
    }

    public void closeAccount(int accountId) {
        int userId;
        User user;
        if (!usersAccounts.containsKey(accountId)) {
            throw new NoSuchElementException("Ошибка: Счета с id " + accountId + " не существует.");
        } else {
            userId = usersAccounts.get(accountId);
        }

        try {
            user = userService.getUserById(userId);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return;
        }

        long countOfAccounts = user.getAccountList()
                .stream()
                .count();
        if (countOfAccounts == 1) {
            throw new DeleteLastUserAccountException(accountId, userId);
        } else {
            BigDecimal moneyAmount = accounts.values().stream()
                    .filter(account -> account.getId() == accountId)
                    .findFirst()
                    .get()
                    .getMoneyAmount();
            user.getAccountList().removeIf(account -> account.getId() == accountId);
            usersAccounts.remove(accountId, userId);
            accounts.remove(accountId);
            user.getAccountList()
                    .getFirst()
                    .addMoney(moneyAmount);
        }
    }

}
