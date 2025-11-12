package nastyazhabko.dev.services;

import nastyazhabko.dev.models.Account;
import nastyazhabko.dev.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class UserService {
    private static List<User> users = new ArrayList<>();
    private static List<String> allLogins = new ArrayList<>();
    private static int idGenerator = 0;
    @Autowired
    private AccountService accountService;

    public void getAllUsers() {
        users.stream().forEach(System.out::println);
    }

    public User addUser(String login) {
        if (allLogins.contains(login)) {
            System.out.println("Ошибка: Пользователь с логином " + login + " уже существует");
            return null;
        } else if (login.isEmpty()) {
            System.out.println("Ошибка: введите непустое значение");
            return null;
        } else {
            List<Account> accountList = new ArrayList<>();
            User user = new User(idGenerator, login, accountList);
            users.add(user);
            accountService.createAccount(idGenerator);
            allLogins.add(login);
            idGenerator++;
            System.out.println("Пользователь успешно создан: " + user);
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
