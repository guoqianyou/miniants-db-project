package db.dbcore.transaction;

import core.util.Assert;
import db.DbErrorCode;
import db.dbcore.cconnection.RDSManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 事物的 线程管理
 * Created by guoqi on 2017/3/13.
 */
public class RsqlThreadManager {
    // 连接的key标记
    private static final String RsqlTransactionConnection = "RsqlTransactionConnection";
    //层级的key 标记
    private final static String RsqlTransactionLevel = "RsqlTransactionLevel";
    // 是否为事物的状态
    private static final String RsqlTransactionStatus = "RsqlTransactionStatus";
    private static ThreadLocal<Map<String, Object>> mapThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Map<String,RsqlThreadTransactionBean>> threadLocal = new ThreadLocal<>();

    // 获取线程中的数据库连接
    public static Connection getConnection(final String spaceName) {
        RsqlThreadTransactionBean transactionBean = getRsqlThreadTransactionBean().get(spaceName);
        Assert.notNull(transactionBean, DbErrorCode.RSQL_THREAD_ERROR, "线程中没有获取到 事物bean！");// RsqlConnectionException.class
        Connection connection = transactionBean.getConnection();
        Assert.notNull(connection, DbErrorCode.RSQL_CONNECTION_ERROR, "初始化事务时没有创建数据库连接，请联系系统管理员！");// RsqlConnectionException.class
        return connection;
    }

    public static Connection createThreadConnection(final String spaceName){
        return  getRsqlThreadTransactionBean()
                .computeIfAbsent(spaceName, s -> new RsqlThreadTransactionBean(s,  RDSManager.getLocalConnection(s))).getConnection();
    }


    public static RsqlThreadTransactionBean getThreadTransactionBean(final String spaceName){
        return getRsqlThreadTransactionBean().get(spaceName);
    }

//    // 将数据库连接 放入当前线程
//    public static void putConnection(final String spaceName, Connection connection) {
//        getRsqlThreadTransactionBean().put(rsqlTransactionConnectionFlagObtain(spaceName), connection);
//    }

    //初始化层级 ,如果没有 则赋值1 如果存在  则+1
    public static void initAndAddLevel(final String spaceName){
        RsqlThreadTransactionBean transactionBean = getRsqlThreadTransactionBean().get(spaceName);
        Assert.notNull(transactionBean, DbErrorCode.RSQL_THREAD_ERROR, "线程中没有获取到 事物bean！");// RsqlConnectionException.class
        transactionBean.setLevel(transactionBean.getLevel()+1);
    }
    // 层级 -1
    public static void minusLevel(final String spaceName){
        RsqlThreadTransactionBean transactionBean = getRsqlThreadTransactionBean().get(spaceName);
        Assert.notNull(transactionBean, DbErrorCode.RSQL_THREAD_ERROR, "线程中没有获取到 事物bean！");// RsqlConnectionException.class
        transactionBean.setLevel(transactionBean.getLevel()+1);
    }

    //获取层级
    public static int getlevel(final String spaceName){
        RsqlThreadTransactionBean transactionBean = getRsqlThreadTransactionBean().get(spaceName);
        Assert.notNull(transactionBean, DbErrorCode.RSQL_THREAD_ERROR, "线程中没有获取到 事物bean！");// RsqlConnectionException.class
        return transactionBean.getLevel();
    }

    public static void setTransactionStatus(final String spaceName,boolean status){
        RsqlThreadTransactionBean transactionBean = getRsqlThreadTransactionBean().get(spaceName);
        Assert.notNull(transactionBean, DbErrorCode.RSQL_THREAD_ERROR, "线程中没有获取到 事物bean！");// RsqlConnectionException.class
        transactionBean.setTransactionStatus(status);
    }

    //移除变量
    public static void remove(final String spaceName){
        getRsqlThreadTransactionBean().putIfAbsent(spaceName,null);
    }


    private static Map<String, RsqlThreadTransactionBean> getRsqlThreadTransactionBean(){
        Map<String, RsqlThreadTransactionBean> transactionBeanMap = threadLocal.get();
        if (transactionBeanMap==null){
            transactionBeanMap = new HashMap<>();
            threadLocal.set(transactionBeanMap);
        }
        return transactionBeanMap;
    }

//    //装配层级的key
//    private static String rsqlTransactionLevelFlagObtain(final String spaceName){
//        return RsqlTransactionLevel +"###"+spaceName;
//    }
//
//    // 装配 连接的key
//    private static String rsqlTransactionConnectionFlagObtain(final String spaceName){
//        return RsqlTransactionConnection +"###"+spaceName;
//    }

}
