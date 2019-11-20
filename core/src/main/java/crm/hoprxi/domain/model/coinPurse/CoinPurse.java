package crm.hoprxi.domain.model.coinPurse;

import crm.hoprxi.domain.model.InsufficientBalanceException;

import javax.money.MonetaryAmount;
import java.util.Objects;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-14
 */
public class CoinPurse {
    private MonetaryAmount balance;
    private Quota quota;

    public CoinPurse(MonetaryAmount balance, Quota quota) {
        setBalance(balance);
        setQuota(quota);
    }

    public CoinPurse(MonetaryAmount balance) {
        this(balance, Quota.ZERO);
    }

    private void setQuota(Quota quota) {
        if (quota == null)
            quota = Quota.ZERO;
        this.quota = quota;
    }

    private void setBalance(MonetaryAmount balance) {
        Objects.requireNonNull(balance, "balance required");
        if (balance.isNegative())
            throw new IllegalArgumentException("balance must large or equal zero");
        this.balance = balance;
    }

    public MonetaryAmount balance() {
        return balance;
    }

    public Quota quota() {
        return quota;
    }

    public Round round(MonetaryAmount receivables) {
        return quota.round(receivables, balance);
    }

    public CoinPurse pay(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "newBalance required");
        if (amount.isNegativeOrZero())
            throw new IllegalArgumentException("pay amount must large zero");
        if(amount.isGreaterThan(balance))
            throw new InsufficientBalanceException("Sorry, your credit is running low");
        return new CoinPurse(balance.subtract(amount), quota);
    }

    public CoinPurse deposit(MonetaryAmount amount) {
        Objects.requireNonNull(amount, "amount required");
        if (amount.isNegativeOrZero())
            throw new IllegalArgumentException("deposit amount must large zero");
        return new CoinPurse(balance.add(amount), quota);
    }

    public CoinPurse changeQuota(Quota newQuota) {
        Objects.requireNonNull(newQuota, "newQuota required");
        if (quota != newQuota)
            return new CoinPurse(balance, newQuota);
        return this;
    }
}
