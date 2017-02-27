package sivan.yue.quarrier.common.exception;

/**
 * Created by xiwen.yxw on 2017/2/27.
 */
public class BlockQueueException extends IllegalStateException{
    public BlockQueueException(String message) {
        super(message);
    }

    public BlockQueueException(String message, Throwable cause) {
        super(message, cause);
    }
}
