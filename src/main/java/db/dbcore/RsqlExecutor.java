package db.dbcore;

import core.exception.ServiceCode;
import db.DbCvo;
import db.DbErrorCode;
import db.DbRvo;
import db.RsqlConstants;
import db.dbcore.cconnection.RDSManager;
import db.dbcore.model.Modelable;
import db.exception.RsqlExecuteException;
import db.sql.NamedParam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by guoqi on 2017/3/10.
 */
public class RsqlExecutor {


    public DbRvo execute(DbCvo dbCvo) {
        RsqlRvo rsqlRvo = new RsqlRvo();
        /* 进入数据库操作 */
        Connection con = null;
        PreparedStatement pstmt = null;
        // 获得数据库连接
        con = RDSManager.getLocalConnection(dbCvo._getSpaceName());
        List<NamedParam> sqlNamedParams = dbCvo._getNamedParams();
        String sqlString = dbCvo._getSqlString();

        StringBuilder msg = new StringBuilder();
        msg.append("In table [ ").append(dbCvo.getBeanName()).append(" ]");

        try {
            // 自动提交设置为False为回滚准备
            // con.setAutoCommit(false); 默认已是

            // 预储存语句
            pstmt = con.prepareStatement(sqlString);
            // 设置参数
            for (NamedParam sqlNamedParam : sqlNamedParams) {
                if (-1 != sqlNamedParam.getIndex())
                    pstmt.setObject(sqlNamedParam.getIndex(), sqlNamedParam.getValue(), sqlNamedParam.getType());
            }

            // 执行数据库操作
            long time = System.currentTimeMillis();
            pstmt.execute();
            msg.append("Execute took:[").append(System.currentTimeMillis() - time).append("ms]");

            // 清除参数关闭与储存
            pstmt.clearParameters();
            // 操作成功后，预储存过程必须关闭,如果关闭前出错，必须后处理关闭工作
            pstmt.close();
            // 如果一切正常则设置ＯＫ状态
//            rsqlRvo.setMsg(true, "数据库操作[execute]数据库操作成功！");
//            rsqlRvo.setStatus(true);
            RDSManager.freeLocalConnection(dbCvo._getSpaceName(), con);
        } catch (SQLException e) {
            // sqlRvo.setMsg(false, "数据库操作[execute]出现异常:", e.toString(),
            // "。回滚数据！");
            // 无论有任何失败后，预储存也必须关闭
            msg = new StringBuilder("SQL异常: ");
            try {
                pstmt.close();
                msg.append("异常后处理PreparedStatement.close()已完成。");
            } catch (Exception e1) {
                msg.append("异常后处理PreparedStatement.close()也发生异常。");
            }
            /**
             * 重新抛出，spring或Aop配置的RsqlAspect配置事务将捕获此异常， 并调用@{link
             * RsqlTransactionalManager#abortTransactional()};处理rollback等事务。
             */
            throw new RsqlExecuteException(DbErrorCode.RSQL_EXECUTE_ERROR, "RsqlExecutor execute 遇到数据库SQL异常", e);
        } finally {
            // 必须清除参数
//            dbCvo.clear();
//            if(!RDBManager.isTransaction(dbCvo._getSpaceName())){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            }
        }
        /* 数据库操作结束 */
        RsqlConstants.logger.info(msg);
        // 返回操作状态
        return rsqlRvo;
    }


    public <T extends Modelable> RsqlRvo<T> executeQuery(final DbCvo<T> dbCvo) {
        RsqlRvo rsqlRvo = new RsqlRvo();
        /* 进入数据库操作 */
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // 获得数据库连接
        con = RDSManager.getLocalConnection(dbCvo._getSpaceName());
        String sqlString = dbCvo._getSqlString();
        List<NamedParam> sqlNamedParams = dbCvo._getNamedParams();
//        Dialect dialect = RDSManager.getLocalSpaceConfig(dbCvo._getSpaceName()).getDialect();
        long time;
        StringBuilder msg = new StringBuilder();

        msg.append("In table [ ").append(dbCvo.getBeanName()).append(" ]");

        try {
            // 自动提交设置为False为回滚准备
            // con.setAutoCommit(false); 默认已是
            sqlString = sqlString + " ";
//                    + Select.obtainSQLOrder(dbCvo);

            String orgSqlString = sqlString; //统计所有记录数时必须使用没有加入分页参数的sql

            int pagination = dbCvo.getPagination();
            int rowCount = dbCvo.getRowCount();
            /******************** 执行查询*******************************/
            time = System.currentTimeMillis();
            if (dbCvo.isDoPaging()) {
                int startId = rowCount * (pagination - 1);
                int endId = rowCount + startId;
//                sqlString = dialect.obtainPagingSQL(sqlString, startId, endId, rowCount);
            }
//            dialect.prepareSqlForCount(sqlString)
            pstmt = con.prepareStatement(sqlString);

            // 设置参数
            for (NamedParam sqlNamedParam : sqlNamedParams) {
                if (-1 != sqlNamedParam.getIndex())
                    pstmt.setObject(sqlNamedParam.getIndex(), sqlNamedParam.getValue(), sqlNamedParam.getType());
            }

            // rs查询
            rs = pstmt.executeQuery();
            msg.append("doQuery took:[").append(System.currentTimeMillis() - time).append("ms];");

            /******************** 保存数据*******************************/
            // 构造并保存结果集合
            time = System.currentTimeMillis();
            rsqlRvo.saveData(rs, dbCvo);
            // 关闭ResultSet
            rs.close();
            // 清除参数关闭与储存
            pstmt.clearParameters();
            pstmt.close();

            /******************** 第一次查询或者茶到最后一页时查询总条数*******************************/
            //此模块必须放在查询之后，以适应mysql中 SELECT SQL_CALC_FOUND_ROWS SELECT FOUND_ROWS() AS count;
            int recordCount = dbCvo.getRecordCount();
            if (dbCvo.isDoCount()) {
                if (recordCount == 0 || pagination == 1 || pagination * rowCount > recordCount) {
                    time = System.currentTimeMillis();
                    pstmt = con.prepareStatement(dialect.obtainCountSql(orgSqlString));

                    if (dialect.needSetParamForCount()) {
                        // 设置参数
                        for (NamedParam sqlNamedParam : sqlNamedParams) {
                            if (-1 != sqlNamedParam.getIndex())
                                pstmt.setObject(sqlNamedParam.getIndex(), sqlNamedParam.getValue(), sqlNamedParam.getType());
                        }
                    }

                    rs = pstmt.executeQuery();
                    if (rs.next())
                        rsqlRvo.setRecordCount(rs.getInt(1));
                    else
                        rsqlRvo.setRecordCount(0);
                    rs.close();
                    pstmt.clearParameters();
                    pstmt.close();
                    msg.append("doCount took:[").append(System.currentTimeMillis() - time).append("ms];");
                } else {
                    rsqlRvo.setRecordCount(recordCount);
                    msg.append("useCount ").append(recordCount).append(" ;");
                }
            }

            msg.append("Read Data Records [").append(rsqlRvo.getRows() != null ? rsqlRvo.getRows().size() : 0).append("], took:[").append(System.currentTimeMillis() - time).append("ms];");
            rsqlRvo.setMsg(true, "数据库操作[executeQuery]数据库操作成功！");
            rsqlRvo.setStatus(true);
            RDSManager.freeLocalConnection(dbCvo._getSpaceName(), con);
        } catch (SQLException e) {
            // 无论有任何失败后，ResultSet也必须关闭
            msg = new StringBuilder("数据库执行Sql过程中发生异常：");
            try {
                if (rs != null) {
                    rs.close();
                    msg.append("异常后处理ResultSet.close()已完成。");
                }
            } catch (Exception e1) {
                msg.append("异常后处理ResultSet.close()也发生异常。");
            }
            // 无论有任何失败后，预储存也必须关闭
            try {
                pstmt.close();
                msg.append("异常后处理PreparedStatement.close()已完成。");
            } catch (Exception e1) {
                msg.append("异常后处理PreparedStatement.close()也发生异常。");
            }
            /**
             * 重新抛出，spring或Aop配置的RsqlAspect配置事务将捕获此异常， 并调用@{link
             * RsqlTransactionalManager#abortTransactional()};处理rollback等事务。
             */
//            rsqlRvo.setMsg(false, "数据库操作[executeQuery]出现异常:" + e.toString());
            throw new RsqlExecuteException(DbErrorCode.RSQL_QUERY_ERROR, "RsqlExecutor executeQuery 遇到数据库SQL异常", e);
        } finally {
            // 必须清除参数
//            dbCvo.clear();
//            if (!RDBManager.isTransaction(dbCvo._getSpaceName())) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            }

        }
        /* 数据库操作结束 */

        // 返回操作状态
        return rsqlRvo;
    }

}
