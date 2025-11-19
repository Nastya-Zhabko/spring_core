package nastyazhabko.dev.models;


import java.math.BigDecimal;

public class Account {
    private final int id;
    private final int userId;
    private BigDecimal moneyAmount;

    public Account(int id, int userId, BigDecimal moneyAmount) {
        this.id = id;
        this.userId = userId;
        this.moneyAmount = moneyAmount;
    }

    public void addMoney(BigDecimal money) {
        if (money.subtract(money).compareTo(BigDecimal.ZERO) > 0) {
            moneyAmount = moneyAmount.add(money);
        } else
            throw new IllegalStateException("Ошибка: На счете меньше средств, чем требуется снять. Текущий баланс: " + moneyAmount);
    }

    public void subtractMoney(BigDecimal money) {
        if (moneyAmount.compareTo(money) >= 0) {
            moneyAmount = moneyAmount.subtract(money);
        } else
            throw new IllegalStateException("Ошибка: На счете меньше средств, чем требуется снять. Текущий баланс: " + moneyAmount);
    }

    public int getId() {
        return id;
    }

    public BigDecimal getMoneyAmount() {
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

