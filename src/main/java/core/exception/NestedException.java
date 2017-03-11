package core.exception;

/**
 * Created by guoqi on 2017/3/10.
 */
public class NestedException extends RuntimeException {

    private static final long serialVersionUID = 6524600802595672393L;
    private String errorCode; //错误代码
    private String errorMsg;//用默认的super.message

    public NestedException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public NestedException(final String errorCode,final String msg, final Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
        this.errorMsg = msg;
    }
}
