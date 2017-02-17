package sivan.yue.quarrier.common.exception;

/**
 * Created by xiwen.yxw on 2017/2/17.
 */
public class AccessOutBoundException extends IllegalStateException{
    public AccessOutBoundException(String message) {
        super(message);
    }

    public AccessOutBoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
