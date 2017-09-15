package core.util;

import core.exception.NestedException;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by guoqi on 2017/3/10.
 */
public class Assert {

    public static void notNull(final Object object, final String errorCode, final String errorMsg) {
        Optional.ofNullable(object).orElseThrow(() -> new NestedException(errorCode, errorMsg));
    }
    public static void notNullAndEmpty(final Object stringOrObject,  final String errorCode, final String errorMsg) {
        if (stringOrObject == null || (stringOrObject instanceof String && "".equals(((String) stringOrObject).trim()))
                || (stringOrObject instanceof Collection && ((Collection) stringOrObject).size()==0)
                || (stringOrObject instanceof Object[] && ((Object[]) stringOrObject).length==0)
                ) {
            throw new NestedException(errorCode, errorMsg);
        }
    }

    //逻辑判断系列方法
    public static void isTrue(final boolean expression, final String errorCode, final String errorMsg) {
        if (!expression) {
            throw new NestedException(errorCode, errorMsg);
        }
    }
}
