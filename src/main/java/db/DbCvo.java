package db;

import db.dbcore.model.Modelable;
import db.sql.NamedParam;

import java.util.List;

/**
 *   RsqlExecutor  sql 执行需要的 装配体
 * Created by guoqi on 2017/3/10.
 */
public class DbCvo<T extends Modelable> {
    //----分页 ---
    private int pagination = 1; //默认1页
    private int rowCount = 100; //默认每页 100 行
    private boolean doPaging = false;
    //----分页 END ---

    private int recordCount; //剩余的条数

    private boolean doCount; // 是否查询剩余条数

    private String spaceName;
    private String beanName;
    private List<NamedParam> _namedParams;
    private String _sqlString;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String _getSpaceName() {
        return spaceName;
    }

    public void _setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public List<NamedParam> _getNamedParams() {
        return _namedParams;
    }

    public void _setNamedParams(List<NamedParam> _namedParams) {
        this._namedParams = _namedParams;
    }

    public String _getSqlString() {
        return this._sqlString;
    }

    public void _setSqlString(String sqlString) {
        this._sqlString = sqlString;
    }

    public int getPagination() {
        return pagination;
    }

    public void setPagination(int pagination) {
        this.pagination = pagination;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public boolean isDoPaging() {
        return doPaging;
    }

    public void setDoPaging(boolean doPaging) {
        this.doPaging = doPaging;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public boolean isDoCount() {
        return doCount;
    }

    public void setDoCount(boolean doCount) {
        this.doCount = doCount;
    }
}
