package db.dbcore.transaction;

import java.sql.Connection;

/**
 * 在事物 线程中记录的bean
 * Created by guoqi on 2017/3/15.
 */
public class RsqlThreadTransactionBean {
    private String spaceName;
    private Connection connection;  //连接
    private int level =1; // 层级
    private boolean transactionStatus; // 是否为 事物


    public RsqlThreadTransactionBean(String spaceName, Connection connection) {
        this.spaceName = spaceName;
        this.connection = connection;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(boolean transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

}
