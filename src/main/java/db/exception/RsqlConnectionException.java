package db.exception;

/**
 * Created by guoqi on 2017/3/10.
 */
public class RsqlConnectionException extends RsqlException {


    public RsqlConnectionException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public RsqlConnectionException(String errorCode, String msg, Throwable cause) {
        super(errorCode, msg, cause);
    }
}
