package nastyazhabko.dev.services;

import nastyazhabko.dev.TransactionHelper;
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
    private final UserService userService;
    private final AccountProperties accountProperties;
    private final TransactionHelper transactionHelper;

    public AccountService(@Lazy UserService userService, AccountProperties accountProperties, TransactionHelper transactionHelper) {
        this.userService = userService;
        this.accountProperties = accountProperties;
        this.transactionHelper = transactionHelper;
    }

    public Account createAccount(User user) {
            Account account = new Account(user, accountProperties.getDefaultAmount());
            return account;
    }

    public Account createAccount(int userId) {

        return transactionHelper.executeInTransaction(session -> {
            User user;
            Account account;
                try {
                    user = userService.getUserById(userId);
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException(e.getMessage());
                }
                account = new Account(user, BigDecimal.ZERO);
                user.addAccount(account);
                session.persist(account);
                session.persist(user);
            return account;
        });

    }

    public Account addMoney(int accountId, BigDecimal money) {
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NullPointerException("Ошибка: Укажите положительную не нулевую сумму!");
        }
        return transactionHelper.executeInTransaction(session -> {
            Account account = session.createQuery("""
                    SELECT a from Account a
                    WHERE a.id = :id
                    """, Account.class).setParameter("id", accountId).uniqueResult();
            if (account == null) {
                throw new NoSuchElementException("Ошибка: Счета с id " + accountId + " не существует.");
            }
                    account.addMoney(money);
                    return account;
        });


    }

    public Account subtractMoney(int accountId, BigDecimal money) {
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NullPointerException("Ошибка: Укажите положительную не нулевую сумму!");
        }

        return transactionHelper.executeInTransaction(session -> {
            int accountIdExists = -1;
            accountIdExists = session.createQuery("""
                    SELECT a from Account a
                    WHERE a.id = :id
                    """, Account.class).setParameter("id", accountId).uniqueResult().getId();
            if (accountIdExists == -1) {
                throw new NoSuchElementException("Ошибка: Счета с id " + accountId + " не существует.");
            }
           Account account = session.createQuery("""
                    SELECT a FROM Account a
                    WHERE a.id=:id
                    """, Account.class).setParameter("id", accountId).uniqueResult();
            account.subtractMoney(money);
            return account;
        });

    }

    public List<Account> transferMoney(int senderAccountId, int recipientAccountId, BigDecimal money) {
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Ошибка: укажите положительную не нулевую сумму!");
        }
        return transactionHelper.executeInTransaction(session -> {
            Account senderAccount;
            Account recipientAccount;

            senderAccount = session.createQuery("""
                    SELECT a from Account a
                    WHERE a.id = :id
                    """, Account.class).setParameter("id", senderAccountId).uniqueResult();
            recipientAccount = session.createQuery("""
                    SELECT a from Account a
                    WHERE a.id = :id
                    """, Account.class).setParameter("id", recipientAccountId).uniqueResult();


            if (senderAccount.getId() == -1 && recipientAccount.getId() == -1) {
                throw new NoSuchElementException("Ошибка: Счета отправителя с id " + senderAccountId + " и счета получателя с id " + recipientAccountId + " не существует.");
            }
            else if (senderAccount.getId() == -1) {
                throw new NoSuchElementException("Ошибка: Счета отправителя с id " + senderAccountId + " не существует.");
            }
            else if (recipientAccount.getId() == -1) {
                throw new NoSuchElementException("Ошибка: Счета получателя с id " + recipientAccountId + " не существует.");
            }
            else if (senderAccount.getId() == recipientAccount.getId()) {
                throw new IllegalArgumentException("Ошибка: В качестве отправителя и получателя указан один счет.");
            }

            if (senderAccount.getUser().getId() == recipientAccount.getUser().getId()) {
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
        });


    }

    public void closeAccount(int accountId) {
        transactionHelper.executeInTransaction(session -> {
            User user;
            Account accountExists;
            accountExists = session.createQuery("""
                    SELECT a from Account a
                    WHERE a.id = :id
                    """, Account.class).setParameter("id", accountId).uniqueResult();

            if (accountExists == null) {
                throw new NoSuchElementException("Ошибка: Счета с id " + accountId + " не существует.");
            } else {
                try {
                    user = accountExists.getUser();
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException("Ошибка, аккаунт " + accountExists + " не связан с пользователем!");
                }
            }
            long countOfAccounts = user.getAccountList()
                    .stream()
                    .count();
            if (countOfAccounts == 1) {
                throw new DeleteLastUserAccountException(accountId, user.getId());
            } else {
                BigDecimal moneyAmount = user.getAccountList().stream()
                        .filter(account -> account.getId() == accountId)
                        .findFirst()
                        .get()
                        .getMoneyAmount();
                user.getAccountList().removeIf(account -> account.getId() == accountId);
                user.getAccountList()
                        .getFirst()
                        .addMoney(moneyAmount);
            }
        });

    }

}
