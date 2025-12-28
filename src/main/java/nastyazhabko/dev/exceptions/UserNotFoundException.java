package nastyazhabko.dev.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("Ошибка: Пользователь с указанным ID: " + id +" не найден!");
    }
}
