package db;

import db.dbcore.model.Modelable;

/**
 * Created by guoqi on 2017/3/10.
 */
public class DbRvo<T extends Modelable> {
    //数据库 执行信息
    private StringBuilder msg = new StringBuilder();

    public StringBuilder getMsg() {
        return msg;
    }
    public StringBuilder appendMsg(final String msg) {
        this.msg.append(msg);
        return this.msg;
    }
}
