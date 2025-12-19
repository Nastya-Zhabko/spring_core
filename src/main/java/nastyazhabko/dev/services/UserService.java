package nastyazhabko.dev.services;

import nastyazhabko.dev.TransactionHelper;
import nastyazhabko.dev.exceptions.LoginAlreadyExistException;
import nastyazhabko.dev.models.Account;
import nastyazhabko.dev.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class UserService {

    private final AccountService accountService;
    private final SessionFactory sessionFactory;
    private final TransactionHelper transactionHelper;

    public UserService(AccountService accountService, SessionFactory sessionFactory, TransactionHelper transactionHelper) {
        this.accountService = accountService;
        this.sessionFactory = sessionFactory;
        this.transactionHelper = transactionHelper;
    }

    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("""
                    SELECT u FROM User u""", User.class).list();
        }
    }

    public User addUser(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Ошибка: введите непустое значение");
        }

        return transactionHelper.executeInTransaction(session -> {
            Optional<User> userWithSameLogin = session.createQuery("""
                    SELECT u FROM User u WHERE u.login = :login
                    """, User.class).setParameter("login", login).stream().findFirst();
            if (userWithSameLogin.isPresent()) {
                throw new LoginAlreadyExistException(login);
            }

            List<Account> accountList = new ArrayList<>();
            User user = new User(login, accountList);
            session.persist(user);
            Account account = accountService.createAccount(user);
            user.getAccountList().add(account);
            session.persist(account);
            return user;
        });
    }

    public User getUserById(int id) {
        return transactionHelper.executeInSession(session -> {
            User user = session.createQuery("""
                    SELECT u from User u
                    WHERE u.id = :id
                    """, User.class).setParameter("id", id).uniqueResult();
            if (!(user == null)) {
                return user;
            } else {
                throw new NoSuchElementException("Ошибка: Пользователя с id " + id + " нет в списке пользователей.");
            }
        });
    }
}
