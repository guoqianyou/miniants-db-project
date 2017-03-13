import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoqi on 2017/3/11.
 */
public class SingleThreadConnectionHolder {

    private  static ThreadLocal<Integer> threadLocal = new InheritableThreadLocal<>();
    private  static ThreadLocal<Map<String,String>> sss = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        threadLocal.set(1111);
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("sdfsdf","dsfdsfsdf");
        sss.set(objectObjectHashMap);
        sss.remove();
        System.out.println("args = [" + args + "]");
    }




}
