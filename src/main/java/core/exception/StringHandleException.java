package core.exception;

/**
 * Created by guoqi on 2017/3/10.
 */
public class StringHandleException extends NestedException {
    private static final long serialVersionUID = -3114114305429927506L;

    public StringHandleException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }
}
