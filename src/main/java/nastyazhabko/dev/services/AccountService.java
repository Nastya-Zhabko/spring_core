package nastyazhabko.dev.services;

import nastyazhabko.dev.properties.AccountProperties;
import nastyazhabko.dev.models.Account;
import nastyazhabko.dev.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AccountService {
    private static Map<Integer, Integer> usersAccounts = new HashMap<>();
    private static List<Account> accounts = new ArrayList<>();
    private static int idGenerator = 0;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountProperties accountProperties;


    public void createAccount(int userId) {
        User user;
        Account account;

        try {
            user = userService.getUserById(userId);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (user.getAccountList().isEmpty()) {
            account = new Account(idGenerator, userId, accountProperties.getDefaultAmount());
        } else {
            account = new Account(idGenerator, userId, 0L);
            System.out.println("Счет успешно создан: " + account);
        }

        accounts.add(account);
        user.addAccount(account);
        usersAccounts.put(idGenerator, userId);
        idGenerator++;

    }

    public void addMoney(int accountId, Long money) {
        if (money <= 0) {
            System.out.println("Укажите положительную не нулевую сумму!");
            return;
        }
        if (!usersAccounts.containsKey(accountId)) {
            System.out.println(("Ошибка: Счета с id " + accountId + " не существует."));
            return;
        } else {
            accounts.get(accountId).addMoney(money);
            System.out.println("Счет " + accountId + " успешно пополнен на " + money + ". Текущий баланс: " + accounts.get(accountId).getMoneyAmount());
        }
    }

    public void subtractMoney(int accountId, Long money) {
        if (money <= 0) {
            System.out.println("Укажите положительную не нулевую сумму!");
            return;
        }
        if (!usersAccounts.containsKey(accountId)) {
            System.out.println(("Ошибка: Счета с id " + accountId + " не существует."));
            return;
        } else {
            try {
                accounts.get(accountId).subtractMoney(money);
                System.out.println("Деньги успешно сняты со счета " + accountId + " Текущий баланс: " + accounts.get(accountId).getMoneyAmount());
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    public void transferMoney(int senderAccountId, int recipientAccountId, Long money) {
        if (money <= 0) {
            System.out.println("Укажите положительную не нулевую сумму!");
            return;
        }
        if (!usersAccounts.containsKey(senderAccountId) && !usersAccounts.containsKey(recipientAccountId)) {
            System.out.println(("Ошибка: Счета отправителя с id " + senderAccountId + " и счета получателя с id " + recipientAccountId + " не существует."));
            return;
        }
        if (!usersAccounts.containsKey(senderAccountId)) {
            System.out.println(("Ошибка: Счета отправителя с id " + senderAccountId + " не существует."));
            return;
        }
        if (!usersAccounts.containsKey(recipientAccountId)) {
            System.out.println(("Ошибка: Счета получателя с id " + recipientAccountId + " не существует."));
            return;
        }
        if (senderAccountId == recipientAccountId) {
            System.out.println(("Ошибка: В качестве отправителя и получателя указан один счет."));
            return;
        }


        Account senderAccount = accounts.get(senderAccountId);
        Account recipientAccount = accounts.get(recipientAccountId);

        if (usersAccounts.get(senderAccountId) == usersAccounts.get(recipientAccountId)) {
            try {
                senderAccount.subtractMoney(money);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                return;
            }

        } else {
            try {
                senderAccount.subtractMoney((long) (money + (money * accountProperties.getDefaultCommission()) / 100));
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        recipientAccount.addMoney(money);
        System.out.println("Деньги успешно переведены со счета " + senderAccountId + " на счет "
                + recipientAccountId + ". \nТекущий баланс на счете отправителя " + senderAccount.getMoneyAmount()
                + ". \nТекущий баланс на счете получателя " + recipientAccount.getMoneyAmount());
    }

    public void closeAccount(int accountId) {
        int userId;
        User user;
        if (!usersAccounts.containsKey(accountId)) {
            System.out.println(("Ошибка: Счета с id " + accountId + " не существует."));
            return;
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
            System.out.println("Ошибка: аккаунт с id "
                    + accountId + " единственный у пользователя "
                    + userId + ". Удаление невозможно!");
            return;
        } else {
            Long moneyAmount = accounts.stream()
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
            System.out.println("Аккаунт успешно закрыт!");
        }
    }

}
