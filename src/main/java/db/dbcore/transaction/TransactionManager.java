package db.dbcore.transaction;

import core.exception.NestedException;
import db.DbErrorCode;
import db.exception.RsqlConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库事物管理中心
 * Created by guoqi on 2017/3/13.
 */
public class TransactionManager {

    /**
     * 事物开始,创建线程的数据库连接， 并且设置自动提交为 false
     */
    public static void start(final String spaceName) {
        //获取事物的连接
        Connection connection = RDSThreadManager.getConnection(spaceName);
        RDSThreadManager.initAndAddLevel(spaceName);
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RsqlConnectionException(DbErrorCode.RSQL_CONNECTION_ERROR, "数据库事务设置为非自动提交异常", e);
        }
    }

    public static void commit(final String spaceName) {
        Connection connection = RDSThreadManager.getConnection(spaceName);
        int getlevel = RDSThreadManager.getlevel(spaceName);
        try {
            if (null != connection && getlevel == 1) {
                connection.commit();
                connection.setAutoCommit(true);
                connection.close();
                RDSThreadManager.remove(spaceName);
            } else {
                RDSThreadManager.minusLevel(spaceName);
            }
        } catch (SQLException e) {
            throw new RsqlConnectionException(DbErrorCode.RSQL_CONNECTION_ERROR, "提交事务失败", e);
        }
    }

    public static void rollback(final Throwable ex, final String spaceName) {
        Connection connection = null;
        try {
            connection = (Connection) RDSThreadManager.getConnectionObj(spaceName);
            if (connection != null) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new NestedException(DbErrorCode.RSQL_CONNECTION_ERROR, "数据库事务中出现非架构异常", ex);
            }
        } catch (Exception e) {
            throw new RsqlConnectionException(DbErrorCode.RSQL_CONNECTION_ERROR, "数据库操作失败，同时回滚操作也失败！" + e.toString(), ex);
        } finally {
            if (null != connection)
                try {
                    connection.close();
                } catch (SQLException e) {
                    //throw new RsqlConnectionException(ServiceCode.RSQL_CONNECTION_ERROR, "数据库关闭错误", e);
                }
            RDSThreadManager.remove(spaceName);
        }
    }

}
