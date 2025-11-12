package nastyazhabko.dev.models;


public class Account {
    private final int id;
    private final int userId;
    private Long moneyAmount;

    public Account(int id, int userId, Long moneyAmount) {
        this.id = id;
        this.userId = userId;
        this.moneyAmount = moneyAmount;
    }

    public void addMoney(Long money) {
        moneyAmount += money;
    }

    public void subtractMoney(Long money) {
        if (moneyAmount >= money) {
            moneyAmount -= money;
        } else
            throw new IllegalStateException("Ошибка: На счете меньше средств, чем требуется снять. Текущий баланс: " + moneyAmount);
    }

    public int getId() {
        return id;
    }

    public Long getMoneyAmount() {
        return moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}

