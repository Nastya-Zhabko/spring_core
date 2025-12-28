package nastyazhabko.dev.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountProperties {
    @Value("${account.default-amount}")
    private BigDecimal defaultAmount;
    @Value("${account.transfer-commission}")
    private BigDecimal defaultCommission;

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public BigDecimal getDefaultCommission() {
        return defaultCommission;
    }
}
