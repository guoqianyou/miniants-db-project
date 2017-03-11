package db.exception;

import core.exception.NestedException;

/**
 * Created by guoqi on 2017/3/10.
 */
public class RsqlException extends NestedException {


    public RsqlException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public RsqlException(String errorCode, String msg, Throwable cause) {
        super(errorCode, msg, cause);
    }
}
