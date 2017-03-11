package db.dbcore.model;

import db.RsqlConstants.DataStatus;

/**
 * Created by guoqi on 2017/3/9.
 */
public interface Modelable {

    /*
	 * 用于添加已被修改的属性，并判断是否什么类型的属性，由于后续的优化
	 */
    String _getModifyFileds();
    /*
	 * 默认返回本类是否是cglib代理的数据库modelbean
	 */
    boolean _isAopModelBean();
    DataStatus _getDataStatus();
    void _setDataStatus(DataStatus dataStatus);
}
