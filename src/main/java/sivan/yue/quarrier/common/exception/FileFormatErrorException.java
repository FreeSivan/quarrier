package sivan.yue.quarrier.common.exception;

/**
 * Created by xiwen.yxw on 2017/2/17.
 */
public class FileFormatErrorException extends IllegalStateException{
    public FileFormatErrorException(String message) {
        super(message);
    }

    public FileFormatErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
