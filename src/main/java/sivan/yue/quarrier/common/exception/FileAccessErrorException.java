package sivan.yue.quarrier.common.exception;

/**
 * Created by xiwen.yxw on 2017/2/24.
 */
public class FileAccessErrorException extends IllegalStateException{
    public FileAccessErrorException(String message) {
        super(message);
    }

    public FileAccessErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
