package db.dbcore.transaction;

import db.dbcore.cconnection.RDSManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 事物的 线程管理
 * Created by guoqi on 2017/3/13.
 */
public class RDSThreadManager {
    // 连接的key标记
    private static final String RsqlTransactionConnectionFlag = "RsqlTransactionConnectionFlag";
    //层级的key 标记
    private final static String RsqlTransactionLevelFlag = "RsqlTransactionLevelFlag";
    private static ThreadLocal<Map<String, Object>> mapThreadLocal = new ThreadLocal<>();

    // 获取线程中的数据库连接
    public static Connection getConnection(final String spaceName) {
        return (Connection) getLocalThreadArgsMap()
                .computeIfAbsent(rsqlTransactionConnectionFlagObtain(spaceName), s -> RDSManager.getLocalConnection(spaceName));
    }

    public static Object getConnectionObj(final String spaceName){
        return getLocalThreadArgsMap().get(rsqlTransactionConnectionFlagObtain(spaceName));
    }

    // 将数据库连接 放入当前线程
    public static void putConnection(final String spaceName, Connection connection) {
        getLocalThreadArgsMap().put(rsqlTransactionConnectionFlagObtain(spaceName), connection);
    }

    //初始化层级 ,如果没有 则赋值1 如果存在  则+1
    public static void initAndAddLevel(final String spaceName){
        getLocalThreadArgsMap().merge(rsqlTransactionLevelFlagObtain(spaceName), 1, (a, b) -> (int)b + (int)a);
    }
    // 层级 -1
    public static void minusLevel(final String spaceName){
        getLocalThreadArgsMap().computeIfPresent(rsqlTransactionLevelFlagObtain(spaceName),(s, o) -> (int)o-1);
    }

    //获取层级
    public static int getlevel(final String spaceName){
       return (int) getLocalThreadArgsMap().get(rsqlTransactionLevelFlagObtain(spaceName));
    }

    //移除变量
    public static void remove(final String spaceName){
        Map<String, Object> localThreadArgsMap = getLocalThreadArgsMap();
        localThreadArgsMap.remove(rsqlTransactionLevelFlagObtain(spaceName));
        localThreadArgsMap.remove(rsqlTransactionConnectionFlagObtain(spaceName));
    }

    /**
     * 获取线程中的 连接map
     *
     * @return 已经存在的连接map 或者 新建
     */
    private static Map<String, Object> getLocalThreadArgsMap() {
        Map<String, Object> stringConnectionMap = mapThreadLocal.get();
        if (stringConnectionMap == null) {
            stringConnectionMap = new HashMap<>();
            mapThreadLocal.set(stringConnectionMap);
        }
        return stringConnectionMap;
    }

    //装配层级的key
    private static String rsqlTransactionLevelFlagObtain(final String spaceName){
        return RsqlTransactionLevelFlag+"###"+spaceName;
    }

    // 装配 连接的key
    private static String rsqlTransactionConnectionFlagObtain(final String spaceName){
        return RsqlTransactionConnectionFlag+"###"+spaceName;
    }

}
