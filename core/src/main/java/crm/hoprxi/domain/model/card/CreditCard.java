package crm.hoprxi.domain.model.card;


import crm.hoprxi.domain.model.card.appearance.Appearance;

import javax.money.MonetaryAmount;
import java.util.Objects;
import java.util.StringJoiner;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-08-15
 */
public class CreditCard extends Card {
    private MonetaryAmount quota;
    private MonetaryAmount balance;

    public CreditCard(String id, String issuerId, String customerId, String password, MonetaryAmount quota, MonetaryAmount balance) {
        this(id, issuerId, customerId, password, TermOfValidity.PERMANENCE, null, quota, balance);
    }

    public CreditCard(String id, String issuerId, String customerId, String password, TermOfValidity termOfValidity, Appearance appearance, MonetaryAmount quota, MonetaryAmount balance) {
        super(id, issuerId, customerId, password, termOfValidity, appearance);
        setQuota(quota);
        setBalance(balance);
    }

    public MonetaryAmount quota() {
        return quota;
    }

    public MonetaryAmount balance() {
        return balance;
    }

    private void setBalance(MonetaryAmount balance) {
        if (balance == null)
            balance = MONETARY_ZERO;
        if (balance.isNegative() && balance.abs().isGreaterThan(quota))
            throw new IllegalArgumentException("above the quota");
        this.balance = balance;
    }

    private void setQuota(MonetaryAmount quota) {
        Objects.requireNonNull(quota, "quota is required");
        if (quota.isNegative())
            throw new IllegalArgumentException("The quota should not be negative");
        this.quota = quota;
    }

    public void pay(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount is required");
        if (amount.isNegativeOrZero())
            return;
        MonetaryAmount temp = this.balance.subtract(amount);
        if (temp.abs().isGreaterThan(quota))
            throw new InsufficientBalanceException("Insufficient available quota.");
        this.balance = temp;
    }

    public void quotaAdjustTo(MonetaryAmount quota) {
        Objects.requireNonNull(quota, "quota is required");
        if (quota.isPositiveOrZero())
            this.quota = quota;
    }

    public void repayment(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount is required");
        if (amount.isPositive())
            this.balance = this.balance.add(amount);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CreditCard.class.getSimpleName() + "[", "]")
                .add("quota=" + quota)
                .add("balance=" + balance)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CreditCard that = (CreditCard) o;

        if (quota != null ? !quota.equals(that.quota) : that.quota != null) return false;
        return balance != null ? balance.equals(that.balance) : that.balance == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (quota != null ? quota.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }
}
