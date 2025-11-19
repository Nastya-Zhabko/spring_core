package nastyazhabko.dev.exceptions;

public class LoginAlreadyExistException extends RuntimeException {
    public LoginAlreadyExistException(String login) {
        super("Ошибка: Пользователь с логином " + login + " уже существует!");
    }
}
