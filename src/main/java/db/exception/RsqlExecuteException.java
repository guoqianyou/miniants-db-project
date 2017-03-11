package db.exception;

/**
 * Created by guoqi on 2017/3/10.
 */
public class RsqlExecuteException extends RsqlException {
    public RsqlExecuteException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public RsqlExecuteException(String errorCode, String msg, Throwable cause) {
        super(errorCode, msg, cause);
    }
}
