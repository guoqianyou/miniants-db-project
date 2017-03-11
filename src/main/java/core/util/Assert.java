package core.util;

import core.exception.NestedException;

import java.util.Optional;

/**
 * Created by guoqi on 2017/3/10.
 */
public class Assert {

    public static void notNull(final Object object, final String errorCode, final String errorMsg) {
        Optional.ofNullable(object).orElseThrow(() -> new NestedException(errorCode, errorMsg));
    }

    //逻辑判断系列方法
    public static void isTrue(final boolean expression, final String errorCode, final String errorMsg) {
        if (!expression) {
            throw new NestedException(errorCode, errorMsg);
        }
    }
}
