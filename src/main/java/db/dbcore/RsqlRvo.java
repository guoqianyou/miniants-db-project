package db.dbcore;

import db.DbCvo;
import db.DbRvo;
import db.dbcore.model.Modelable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by guoqi on 2017/3/10.
 */
public class RsqlRvo<T extends Modelable> extends DbRvo<T> {

    private String id = null;
    private int recordCount; //剩余的条数

    /*
	 * 内部函数 用于根据RemexSqlCvo的要求把ResultSet里面的数据保存下来
	 * 目前存在的问题：保存条目的总数this.allSize的时候是用的rs.last() &amp; rs.getRow()效率极低。
	 */
    public void saveData(final ResultSet resultSet, final DbCvo<T> dbCvo) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();

    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
}
