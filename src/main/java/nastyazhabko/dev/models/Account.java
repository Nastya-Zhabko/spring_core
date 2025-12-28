package nastyazhabko.dev.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "money_amount")
    private BigDecimal moneyAmount;

    public Account(User user, BigDecimal moneyAmount) {
        this.user = user;
        this.moneyAmount = moneyAmount;
    }

    public Account() {
    }

    public void addMoney(BigDecimal money) {
        if (moneyAmount.add(money).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Ошибка: На счете меньше средств, чем требуется снять. Текущий баланс: " + moneyAmount);
        }
        moneyAmount = moneyAmount.add(money);
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

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}

