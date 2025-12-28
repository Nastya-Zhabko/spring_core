package nastyazhabko.dev.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login")
    private String login;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Account> accountList;

    public User(String login, List<Account> accountList) {
        this.login = login;
        this.accountList = accountList;
    }

    public User() {
    }

    public void addAccount(Account account) {
        accountList.add(account);
    }

    public int getId() {
        return id;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accountList=" + accountList +
                '}';
    }
}
