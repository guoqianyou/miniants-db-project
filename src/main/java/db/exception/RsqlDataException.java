package db.exception;

/**
 * Created by guoqi on 2017/3/15.
 */
public class RsqlDataException extends RsqlException {
    public RsqlDataException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public RsqlDataException(String errorCode, String msg, Throwable cause) {
        super(errorCode, msg, cause);
    }
}
