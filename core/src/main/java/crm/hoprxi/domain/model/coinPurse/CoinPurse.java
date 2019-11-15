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

    public CoinPurse(MonetaryAmount balance) {
        this.balance = balance;
    }

    public MonetaryAmount balance() {
        return balance;
    }
}
