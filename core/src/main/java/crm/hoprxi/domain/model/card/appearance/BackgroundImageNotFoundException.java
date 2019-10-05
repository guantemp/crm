package crm.hoprxi.domain.model.card.appearance;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-08-15
 */
public class BackgroundImageNotFoundException extends RuntimeException {
    public BackgroundImageNotFoundException(String message) {
        super(message, null, false, false);
    }
}
