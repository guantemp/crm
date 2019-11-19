package crm.hoprxi.domain.model.coinPurse;

import javax.money.MonetaryAmount;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-14
 */
public class CoinPurse {
    private MonetaryAmount balance;
    private Quota quota;

    public CoinPurse(MonetaryAmount balance, Quota quota) {
        this.balance = balance;
        this.quota = quota;
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

    public CoinPurse changeBalance() {
        return this;
    }

    public CoinPurse changeQuota() {
        return this;
    }
}
