package sivan.yue.quarrier.common.exception;

/**
 * Created by xiwen.yxw on 2017/2/24.
 */
public class FileNotFindException extends IllegalStateException{
    public FileNotFindException(String message) {
        super(message);
    }

    public FileNotFindException(String message, Throwable cause) {
        super(message, cause);
    }
}
