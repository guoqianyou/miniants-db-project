package db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by guoqi on 2017/3/8.
 */
public class RsqlConstants {
    public static Logger logger = LogManager.getLogger(RsqlConstants.class);

    public enum RDSType {
        MYSQL,
        ORACLE
    }

    /**
     * 数据状态
     */
    public enum DataStatus {
        DS_saving,  //正在保存

    }

    public enum SqlOper{
        /**增加*/
        add,
        /**删除*/
        del,
        /**编辑*/
        edit,
        /**执行*/
        execute,
        /** 查看数据 **/
        list,
        /**数据存储*/
        store,
        /**数据查看*/
        view,
        /**复制*/
        copy,
        /***/
        sql
    }
}
