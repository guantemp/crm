package crm.hoprxi.domain.model.card;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-07-26
 */
public class InsufficientBalanceException extends RuntimeException {
    /**
     * 仅包含message, 没有cause, 也不记录栈异常, 性能最高
     *
     * @param message the message
     */
    public InsufficientBalanceException(String message) {
        super(message, null, false, false);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause, false, true);
    }
}
