package nastyazhabko.dev.services;

import nastyazhabko.dev.exceptions.LoginAlreadyExistException;
import nastyazhabko.dev.models.Account;
import nastyazhabko.dev.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class UserService {
    private List<User> users = new ArrayList<>();
    private List<String> allLogins = new ArrayList<>();
    private int idGenerator = 0;

    private final AccountService accountService;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User addUser(String login) {
        if (allLogins.contains(login)) {
            throw new LoginAlreadyExistException(login);
        } else if (login.trim().isEmpty() || login == null) {
            throw new IllegalArgumentException("Ошибка: введите непустое значение");
        } else {
            List<Account> accountList = new ArrayList<>();
            User user = new User(idGenerator, login, accountList);
            users.add(user);
            accountService.createAccount(idGenerator);
            allLogins.add(login);
            idGenerator++;
            return user;
        }
    }

    public User getUserById(int id) {
        Optional user = users.stream().filter(u -> u.getId() == id).findFirst();
        if (user.isPresent()) {
            return (User) user.get();
        } else {
            throw new NoSuchElementException("Ошибка: Пользователя с id " + id + " нет в списке пользователей.");
        }
    }
}
