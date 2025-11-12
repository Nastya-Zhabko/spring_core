package nastyazhabko.dev.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountProperties {
    private final int defaultAmount;
    private final int defaultCommission;

    public AccountProperties(@Value("${account.default-amount}") int defaultAmount, @Value("${account.transfer-commission}") int defaultCommission) {
        this.defaultAmount = defaultAmount;
        this.defaultCommission = defaultCommission;
    }

    public long getDefaultAmount() {
        return defaultAmount;
    }

    public int getDefaultCommission() {
        return defaultCommission;
    }
}
