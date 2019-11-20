package crm.hoprxi.domain.model;

/***
 * @author <a href="www.hoprxi.com/authors/guan xiangHuan">guan xiangHuang</a>
 * @since JDK8.0
 * @version 0.0.1 2019-11-20
 */
public class InsufficientBalanceException extends RuntimeException {
    /**
     * 仅包含message, 没有cause, 不记录栈异常, 性能最高
     *
     * @param message
     */
    public InsufficientBalanceException(String message) {
        super(message, null, false, false);
    }

    /**
     * @param message
     * @param cause
     */
    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause, false, true);
    }
}
