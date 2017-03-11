package core.util;

import core.exception.ServiceCode;
import core.exception.StringHandleException;

import java.lang.reflect.Type;

/**
 * Created by guoqi on 2017/3/10.
 */
public class StringHelper {


    /**
     * 为类简名
     * TODO spring3 中 代理类 的类名 处理
     *
     * @param type 类名
     * @return String 返回的类简名
     */

    public static String getClassSimpleName(final Type type) {
        if (type instanceof Class<?>) {
            String sn = ((Class<?>) type).getSimpleName();
            return sn.split("\\$\\$EnhancerByCGLIB\\$\\$")[0];
        } else {
            throw new StringHandleException(ServiceCode.FAIL, "因为此Type非Class类型，无法为Type" + type.toString() + "类简名！");
            // return null;//never arrived here
        }
    }

    /**
     * 处理 CGLIBC 代理类名字的方法
     *
     * @param cglibClassName 代理的类的名字
     * @return 正常类的名字
     */
    public static String handleCGLIBClassName(String cglibClassName) {
        Assert.notNull(cglibClassName,ServiceCode.FAIL,"代理类的名字错误！");
        return cglibClassName.split("\\$\\$EnhancerByCGLIB\\$\\$")[0];
    }

}
