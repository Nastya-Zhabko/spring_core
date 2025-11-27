package nastyazhabko.dev.exceptions;

public class DeleteLastUserAccountException extends RuntimeException {
    public DeleteLastUserAccountException(int accountId, int userId) {
        super("Ошибка: аккаунт с id "
                + accountId + " единственный у пользователя "
                + userId + ". Удаление невозможно!");
    }
}
