package core.util;

/**
 * Created by guoqi on 2017/3/10.
 */
public class Judgment {
    /**
     * 判断一个对象是null或""
     * @param vStr java对象
     * @return boolean 布尔值
     */
    public static boolean nullOrBlank (Object vStr){
        return null == vStr || "".equals(vStr);
    }
}
