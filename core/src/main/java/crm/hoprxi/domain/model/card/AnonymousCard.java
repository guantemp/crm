package crm.hoprxi.domain.model.card;

import crm.hoprxi.domain.model.card.appearance.Appearance;
import crm.hoprxi.domain.model.coinPurse.CoinPurse;
import crm.hoprxi.domain.model.integral.Integral;

import javax.money.MonetaryAmount;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-14
 */
public class AnonymousCard extends Card {
    private MonetaryAmount principal;
    private MonetaryAmount give;
    private CoinPurse coinPurse;
    private Integral integral;

    public AnonymousCard(String id, String issuerId, String password, TermOfValidity termOfValidity, Appearance appearance) {
        super(id, issuerId, password, termOfValidity, appearance);
    }
}
