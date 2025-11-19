package nastyazhabko.dev.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountProperties {
    private final BigDecimal defaultAmount;
    private final BigDecimal defaultCommission;

    public AccountProperties(@Value("${account.default-amount}") BigDecimal defaultAmount, @Value("${account.transfer-commission}") BigDecimal defaultCommission) {
        this.defaultAmount = defaultAmount;
        this.defaultCommission = defaultCommission;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public BigDecimal getDefaultCommission() {
        return defaultCommission;
    }
}
