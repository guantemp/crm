package crm.hoprxi.domain.model.card;

import mi.hoprxi.id.ObjectId;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-08-20
 */
public interface DebitCardRepository {
    void save(DebitCard card);

    default String nextIdentity() {
        return new ObjectId().id();
    }

    DebitCard authenticCredentials(String number, String password);

    void remove(String id);

    DebitCard find(String id);

    DebitCard findByCustomer(String customerId);

    DebitCard findByTelephone(String telephone);
}
